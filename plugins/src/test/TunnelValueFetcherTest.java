package m32plugin;

import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.std.wiring.Tunnel;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.comp.ComponentFactory;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import m32plugin.TunnelValueFetcher;

@RunWith(MockitoJUnitRunner.class)
public class TunnelValueFetcherTest {

    @Mock
    private CircuitState circuitState;

    @Mock
    private Circuit circuit;

    @Mock
    private Component tunnelComponent;

    @Mock
    private Component nonTunnelComponent;

    @Mock
    private AttributeSet attributeSet;

    @Mock
    private Tunnel tunnelFactory;

    private TunnelValueFetcher tunnelValueFetcher;

    @Before
    public void setUp() {
        tunnelValueFetcher = new TunnelValueFetcher();
        when(circuitState.getCircuit()).thenReturn(circuit);
    }

    @Test
    public void testGetTunnelValue_WhenTunnelExistsWithMatchingLabel_ReturnsValue() {
        // Arrange
        String tunnelLabel = "testTunnel";
        Value expectedValue = Value.TRUE;
        Location location = Location.create(10, 20, false);
        Set<Component> tunnelComponents = new HashSet<Component>();
        
        tunnelComponents.add(nonTunnelComponent);
		tunnelComponents.add(tunnelComponent);

        when(circuit.getNonWires()).thenReturn(tunnelComponents);
        when(tunnelComponent.getFactory()).thenReturn(tunnelFactory);
        when(tunnelComponent.getAttributeSet()).thenReturn(attributeSet);
        when(attributeSet.getValue(StdAttr.LABEL)).thenReturn(tunnelLabel);
        when(tunnelComponent.getLocation()).thenReturn(location);
        when(circuitState.getValue(location)).thenReturn(expectedValue);

        // Act
        Value result = tunnelValueFetcher.getTunnelValue(circuitState, tunnelLabel);

        // Assert
        assertEquals(expectedValue, result);
        verify(circuitState).getValue(location);
    }

    @Test
    public void testGetTunnelValue_WhenTunnelExistsWithDifferentLabel_ReturnsUnknown() {
        // Arrange
        String targetLabel = "targetTunnel";
        String actualLabel = "differentTunnel";        
        Set<Component> tunnelComponents = new HashSet<Component>();
		
		tunnelComponents.add(tunnelComponent);

        when(circuit.getNonWires()).thenReturn(tunnelComponents);
        when(tunnelComponent.getFactory()).thenReturn(tunnelFactory);
        when(tunnelComponent.getAttributeSet()).thenReturn(attributeSet);
        when(attributeSet.getValue(StdAttr.LABEL)).thenReturn(actualLabel);

        // Act
        Value result = tunnelValueFetcher.getTunnelValue(circuitState, targetLabel);

        // Assert
        assertEquals(Value.UNKNOWN, result);
        verify(circuitState, never()).getValue(any(Location.class));
    }

    @Test
    public void testGetTunnelValue_WhenNoTunnelComponents_ReturnsUnknown() {
        // Arrange
        String tunnelLabel = "testTunnel";
		Set<Component> tunnelComponents = new HashSet<Component>();
		
		tunnelComponents.add(tunnelComponent);

        when(circuit.getNonWires()).thenReturn(tunnelComponents);
        when(nonTunnelComponent.getFactory()).thenReturn(mock(ComponentFactory.class)); // Not a Tunnel

        // Act
        Value result = tunnelValueFetcher.getTunnelValue(circuitState, tunnelLabel);

        // Assert
        assertEquals(Value.UNKNOWN, result);
        verify(circuitState, never()).getValue(any(Location.class));
    }

    @Test
    public void testGetTunnelValue_WhenEmptyCircuit_ReturnsUnknown() {
        // Arrange
        String tunnelLabel = "testTunnel";        

        when(circuit.getNonWires()).thenReturn(Collections.emptySet());

        // Act
        Value result = tunnelValueFetcher.getTunnelValue(circuitState, tunnelLabel);

        // Assert
        assertEquals(Value.UNKNOWN, result);
        verify(circuitState, never()).getValue(any(Location.class));
    }

    @Test
    public void testGetTunnelValue_WhenMultipleTunnelsFindsCorrectOne() {
        // Arrange
        String targetLabel = "correctTunnel";
        String wrongLabel1 = "wrongTunnel1";
        String wrongLabel2 = "wrongTunnel2";
        
        Value expectedValue = Value.FALSE;
        Location targetLocation = Location.create(30, 40, false);

        Component tunnel1 = mock(Component.class);
        Component tunnel2 = mock(Component.class);
        Component tunnel3 = mock(Component.class);

        AttributeSet attr1 = mock(AttributeSet.class);
        AttributeSet attr2 = mock(AttributeSet.class);
        AttributeSet attr3 = mock(AttributeSet.class);
        
        Set<Component> tunnelComponents = new HashSet<Component>();
		
		tunnelComponents.add(tunnel1);
		tunnelComponents.add(tunnel2);
		tunnelComponents.add(tunnel3);

        when(circuit.getNonWires()).thenReturn(tunnelComponents);
        
        // Tunnel 1 (wrong)
        when(tunnel1.getFactory()).thenReturn(tunnelFactory);
        when(tunnel1.getAttributeSet()).thenReturn(attr1);
        when(attr1.getValue(StdAttr.LABEL)).thenReturn(wrongLabel1);
        
        // Tunnel 2 (correct)
        when(tunnel2.getFactory()).thenReturn(tunnelFactory);
        when(tunnel2.getAttributeSet()).thenReturn(attr2);
        when(attr2.getValue(StdAttr.LABEL)).thenReturn(targetLabel);
        when(tunnel2.getLocation()).thenReturn(targetLocation);
        
        // Tunnel 3 (wrong)
        when(tunnel3.getFactory()).thenReturn(tunnelFactory);
        when(tunnel3.getAttributeSet()).thenReturn(attr3);
        when(attr3.getValue(StdAttr.LABEL)).thenReturn(wrongLabel2);

        when(circuitState.getValue(targetLocation)).thenReturn(expectedValue);

        // Act
        Value result = tunnelValueFetcher.getTunnelValue(circuitState, targetLabel);

        // Assert
        assertEquals(expectedValue, result);
        verify(circuitState).getValue(targetLocation);
        // Verify we don't check locations of the wrong tunnels
        verify(circuitState, never()).getValue(any(Location.class));
    }

    @Test
    public void testGetTunnelValue_WhenTunnelLabelIsNull_ReturnsUnknown() {
        // Arrange
        String tunnelLabel = "testTunnel";
        
		Set<Component> tunnelComponents = new HashSet<Component>();
		tunnelComponents.add(tunnelComponent);

        when(circuit.getNonWires()).thenReturn(tunnelComponents);
        when(tunnelComponent.getFactory()).thenReturn(tunnelFactory);
        when(tunnelComponent.getAttributeSet()).thenReturn(attributeSet);
        when(attributeSet.getValue(StdAttr.LABEL)).thenReturn(null);

        // Act
        Value result = tunnelValueFetcher.getTunnelValue(circuitState, tunnelLabel);

        // Assert
        assertEquals(Value.UNKNOWN, result);
        verify(circuitState, never()).getValue(any(Location.class));
    }

    @Test
    public void testGetTunnelValue_WhenSearchingWithNullLabel_ReturnsUnknown() {
		Set<Component> tunnelComponents = new HashSet<Component>();
		
		tunnelComponents.add(tunnelComponent);
		
        // Arrange
        when(circuit.getNonWires()).thenReturn(tunnelComponents);
        when(tunnelComponent.getFactory()).thenReturn(tunnelFactory);
        when(tunnelComponent.getAttributeSet()).thenReturn(attributeSet);
        when(attributeSet.getValue(StdAttr.LABEL)).thenReturn("someLabel");

        // Act
        Value result = tunnelValueFetcher.getTunnelValue(circuitState, null);

        // Assert
        assertEquals(Value.UNKNOWN, result);
        verify(circuitState, never()).getValue(any(Location.class));
    }
}
