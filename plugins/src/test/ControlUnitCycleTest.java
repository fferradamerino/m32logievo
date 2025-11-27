package m32plugin;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;

import m32plugin.ControlUnitCycle;

@RunWith(MockitoJUnitRunner.class)
public class ControlUnitCycleTest {

    private ControlUnitCycle controlUnit;
    
    @Mock
    private InstancePainter painter;
    
    @Mock
    private InstanceState state;
    
    @Mock
    private Instance instance;
    
    @Mock
    private Graphics graphics;
    
    @Mock
    private FontMetrics fontMetrics;
    
    @Before
    public void setUp() {
        controlUnit = new ControlUnitCycle();
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(controlUnit);
        assertEquals("ControlUnitCycle", controlUnit.getName());
    }

    @Test
    public void testPaintInstance_Fetch1() {
        testPaintWithAddress(0, "FETCH 1");
    }
    
    @Test
    public void testPaintInstance_Fetch2_Address1() {
        testPaintWithAddress(1, "FETCH 2");
    }
    
    @Test
    public void testPaintInstance_Fetch2_Address2() {
        testPaintWithAddress(2, "FETCH 2");
    }
    
    @Test
    public void testPaintInstance_Decod_Address3() {
        testPaintWithAddress(3, "DECOD");
    }
    
    @Test
    public void testPaintInstance_Decod_Address4() {
        testPaintWithAddress(4, "DECOD");
    }
    
    @Test
    public void testPaintInstance_Execute() {
        testPaintWithAddress(5, "EXECUTE");
    }
    
    @Test
    public void testPaintInstance_UndefinedAddress() {
        // Setup
        Value undefinedValue = Value.createUnknown(com.cburch.logisim.data.BitWidth.create(8));
        Bounds bounds = Bounds.create(0, 0, 200, 30);
        
        when(painter.getInstance()).thenReturn(instance);
        when(painter.getGraphics()).thenReturn(graphics);
        when(painter.getPortValue(0)).thenReturn(undefinedValue);
        when(instance.getBounds()).thenReturn(bounds);
        when(graphics.getFontMetrics()).thenReturn(fontMetrics);
        when(fontMetrics.stringWidth(anyString())).thenReturn(100);
        
        // Execute
        controlUnit.paintInstance(painter);
        
        // Verify
        verify(painter).drawBounds();
        verify(painter).drawPorts();
        verify(graphics).setColor(Color.BLACK);
        verify(graphics).drawString(eq("Dirección desconocida"), anyInt(), anyInt());
    }
    
    @Test
    public void testPropagate() {
        // Este método no hace nada, pero lo probamos para coverage
        controlUnit.propagate(state);
        // No hay verificaciones porque el método está vacío
    }
    
    // Método helper para reducir duplicación
    private void testPaintWithAddress(int address, String expectedText) {
        // Setup
        Value addrValue = Value.createKnown(
            com.cburch.logisim.data.BitWidth.create(8), 
            address
        );
        Bounds bounds = Bounds.create(0, 0, 200, 30);
        
        when(painter.getInstance()).thenReturn(instance);
        when(painter.getGraphics()).thenReturn(graphics);
        when(painter.getPortValue(0)).thenReturn(addrValue);
        when(instance.getBounds()).thenReturn(bounds);
        when(graphics.getFontMetrics()).thenReturn(fontMetrics);
        when(fontMetrics.stringWidth(anyString())).thenReturn(50);
        
        // Execute
        controlUnit.paintInstance(painter);
        
        // Verify
        verify(painter).drawBounds();
        verify(painter).drawPorts();
        verify(graphics).setColor(Color.BLACK);
        verify(graphics).drawString(eq(expectedText), anyInt(), anyInt());
    }
}