package m32plugin;

import com.cburch.logisim.data.*;
import com.cburch.logisim.instance.*;
import com.cburch.logisim.circuit.CircuitState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import m32plugin.UnifiedWire;
import m32plugin.TunnelValueFetcher;
import m32plugin.AuxWire;

/**
 * Test class for UnifiedWire component using JUnit 4 and Mockito
 * Tests all major functionality including port configuration, propagation,
 * wire thickness calculation, and color selection
 */
@RunWith(MockitoJUnitRunner.class)
public class UnifiedWireTest {

    // Class under test
    private UnifiedWire unifiedWire;

    // Mocked dependencies
    @Mock
    private Instance mockInstance;
    
    @Mock
    private InstanceState mockInstanceState;
    
    @Mock
    private InstancePainter mockInstancePainter;
    
    @Mock
    private AttributeSet mockAttributeSet;
    
    @Mock
    private Graphics mockGraphics;
    
    @Mock
    private TunnelValueFetcher mockTunnelValueFetcher;
    
    @Mock
    private AuxWire mockAuxWire;
    
    @Mock
    private Bounds mockBounds;
    
    @Mock
    private CircuitState mockCircuitState;

    @Before
    public void setUp() {
        // Initialize the class under test
        unifiedWire = new UnifiedWire();
        
        // Inject mocked dependencies
        unifiedWire.tunnelValueFetcher = mockTunnelValueFetcher;
    }

    // ========== CONSTRUCTOR TESTS ==========
    
    @Test
    public void testConstructor_InitializesCorrectly() {
        UnifiedWire wire = new UnifiedWire();
        
        assertNotNull("UnifiedWire should not be null", wire);
        assertNotNull("TunnelValueFetcher should be initialized", wire.tunnelValueFetcher);
    }

    // ========== PROPAGATE TESTS ==========
    
    @Test
    public void testPropagate_CopiesInputToOutput() {
        // Setup
        Value inputValue = Value.createKnown(32, 42);
        when(mockInstanceState.getPortValue(0)).thenReturn(inputValue);
        
        // Act
        unifiedWire.propagate(mockInstanceState);
        
        // Assert
        verify(mockInstanceState, times(1)).setPort(eq(1), eq(inputValue), eq(1));
    }
    
    @Test
    public void testPropagate_WithZeroValue() {
        // Setup
        Value inputValue = Value.createKnown(32, 0);
        when(mockInstanceState.getPortValue(0)).thenReturn(inputValue);
        
        // Act
        unifiedWire.propagate(mockInstanceState);
        
        // Assert
        verify(mockInstanceState, times(1)).setPort(eq(1), eq(inputValue), eq(1));
    }
    
    @Test
    public void testPropagate_WithMaxValue() {
        // Setup
        Value inputValue = Value.createKnown(32, 0xFFFFFFFF);
        when(mockInstanceState.getPortValue(0)).thenReturn(inputValue);
        
        // Act
        unifiedWire.propagate(mockInstanceState);
        
        // Assert
        verify(mockInstanceState, times(1)).setPort(eq(1), eq(inputValue), eq(1));
    }

    // ========== GET WIRE THICKNESS TESTS ==========
    
    @Test
    public void testGetWireThickness_1Bit_Returns2() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getWireThickness", BitWidth.class);
        method.setAccessible(true);
        
        // Act
        int thickness = (int) method.invoke(unifiedWire, BitWidth.create(1));
        
        // Assert
        assertEquals("1-bit wire should have thickness 2", 2, thickness);
    }
    
    @Test
    public void testGetWireThickness_8Bit_Returns3() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getWireThickness", BitWidth.class);
        method.setAccessible(true);
        
        // Act
        int thickness = (int) method.invoke(unifiedWire, BitWidth.create(8));
        
        // Assert
        assertEquals("8-bit wire should have thickness 3", 3, thickness);
    }
    
    @Test
    public void testGetWireThickness_16Bit_Returns4() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getWireThickness", BitWidth.class);
        method.setAccessible(true);
        
        // Act
        int thickness = (int) method.invoke(unifiedWire, BitWidth.create(16));
        
        // Assert
        assertEquals("16-bit wire should have thickness 4", 4, thickness);
    }
    
    @Test
    public void testGetWireThickness_32Bit_Returns5() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getWireThickness", BitWidth.class);
        method.setAccessible(true);
        
        // Act
        int thickness = (int) method.invoke(unifiedWire, BitWidth.create(32));
        
        // Assert
        assertEquals("32-bit wire should have thickness 5", 5, thickness);
    }
    
    @Test
    public void testGetWireThickness_64Bit_Returns6() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getWireThickness", BitWidth.class);
        method.setAccessible(true);
        
        // Act
        int thickness = (int) method.invoke(unifiedWire, BitWidth.create(64));
        
        // Assert
        assertEquals("64-bit wire should have thickness 6", 6, thickness);
    }

    // ========== GET COLOR FOR ALU VALUE TESTS ==========
    
    @Test
    public void testGetColorForALUValue_0_ReturnsRed() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getColorForALUValue", Value.class);
        method.setAccessible(true);
        
        // Act
        Color color = (Color) method.invoke(unifiedWire, Value.createKnown(4, 0));
        
        // Assert
        assertEquals("ALU value 0 should be RED", Color.RED, color);
    }
    
    @Test
    public void testGetColorForALUValue_1_ReturnsOrange() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getColorForALUValue", Value.class);
        method.setAccessible(true);
        
        // Act
        Color color = (Color) method.invoke(unifiedWire, Value.createKnown(4, 1));
        
        // Assert
        assertEquals("ALU value 1 should be ORANGE", Color.ORANGE, color);
    }
    
    @Test
    public void testGetColorForALUValue_3_ReturnsYellow() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getColorForALUValue", Value.class);
        method.setAccessible(true);
        
        // Act
        Color color = (Color) method.invoke(unifiedWire, Value.createKnown(4, 3));
        
        // Assert
        assertEquals("ALU value 3 should be YELLOW", Color.YELLOW, color);
    }
    
    @Test
    public void testGetColorForALUValue_5_ReturnsGreen() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getColorForALUValue", Value.class);
        method.setAccessible(true);
        
        // Act
        Color color = (Color) method.invoke(unifiedWire, Value.createKnown(4, 5));
        
        // Assert
        assertEquals("ALU value 5 should be GREEN", Color.GREEN, color);
    }
    
    @Test
    public void testGetColorForALUValue_7_ReturnsCyan() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getColorForALUValue", Value.class);
        method.setAccessible(true);
        
        // Act
        Color color = (Color) method.invoke(unifiedWire, Value.createKnown(4, 7));
        
        // Assert
        assertEquals("ALU value 7 should be CYAN", Color.CYAN, color);
    }
    
    @Test
    public void testGetColorForALUValue_9_ReturnsBlue() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getColorForALUValue", Value.class);
        method.setAccessible(true);
        
        // Act
        Color color = (Color) method.invoke(unifiedWire, Value.createKnown(4, 9));
        
        // Assert
        assertEquals("ALU value 9 should be BLUE", Color.BLUE, color);
    }
    
    @Test
    public void testGetColorForALUValue_12_ReturnsMagenta() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getColorForALUValue", Value.class);
        method.setAccessible(true);
        
        // Act
        Color color = (Color) method.invoke(unifiedWire, Value.createKnown(4, 12));
        
        // Assert
        assertEquals("ALU value 12 should be MAGENTA", Color.MAGENTA, color);
    }
    
    @Test
    public void testGetColorForALUValue_13_ReturnsPink() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getColorForALUValue", Value.class);
        method.setAccessible(true);
        
        // Act
        Color color = (Color) method.invoke(unifiedWire, Value.createKnown(4, 13));
        
        // Assert
        assertEquals("ALU value 13 should be PINK", Color.PINK, color);
    }
    
    @Test
    public void testGetColorForALUValue_Invalid_ReturnsGray() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getColorForALUValue", Value.class);
        method.setAccessible(true);
        
        // Act
        Color color = (Color) method.invoke(unifiedWire, Value.createKnown(5, 17));
        
        // Assert
        assertEquals("Invalid ALU value should be GRAY", Color.GRAY, color);
    }

    // ========== GET COLOR FOR DATA VALUE TESTS ==========
    
    @Test
    public void testGetColorForDataValue_FullyDefined_ReturnsMagenta() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getColorForDataValue", Value.class);
        method.setAccessible(true);
        
        // Act
        Value definedValue = Value.createKnown(32, 42);
        Color color = (Color) method.invoke(unifiedWire, definedValue);
        
        // Assert
        assertEquals("Fully defined value should be MAGENTA", Color.MAGENTA, color);
    }
    
    @Test
    public void testGetColorForDataValue_Undefined_ReturnsGray() throws Exception {
        // Use reflection to access private method
        Method method = UnifiedWire.class.getDeclaredMethod("getColorForDataValue", Value.class);
        method.setAccessible(true);
        
        // Act
        Value undefinedValue = Value.createUnknown(BitWidth.create(32));
        Color color = (Color) method.invoke(unifiedWire, undefinedValue);
        
        // Assert
        assertEquals("Undefined value should be GRAY", Color.GRAY, color);
    }

    // ========== GET OFFSET BOUNDS TESTS ==========
    
    @Test
    public void testGetOffsetBounds_EastFacing() {
        // Setup
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_FACING)).thenReturn(Direction.EAST);
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_WIDTH)).thenReturn(100);
        
        // Act
        Bounds bounds = unifiedWire.getOffsetBounds(mockAttributeSet);
        
        // Assert
        assertNotNull("Bounds should not be null", bounds);
        assertEquals("X should be 0", 0, bounds.getX());
        assertEquals("Y should be -10", -10, bounds.getY());
        assertEquals("Width should be 100", 100, bounds.getWidth());
        assertEquals("Height should be 20", 20, bounds.getHeight());
    }
    
    @Test
    public void testGetOffsetBounds_WestFacing() {
        // Setup
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_FACING)).thenReturn(Direction.WEST);
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_WIDTH)).thenReturn(150);
        
        // Act
        Bounds bounds = unifiedWire.getOffsetBounds(mockAttributeSet);
        
        // Assert
        assertNotNull("Bounds should not be null", bounds);
        assertEquals("Width should be 150", 150, bounds.getWidth());
    }
    
    @Test
    public void testGetOffsetBounds_NorthFacing() {
        // Setup
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_FACING)).thenReturn(Direction.NORTH);
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_WIDTH)).thenReturn(200);
        
        // Act
        Bounds bounds = unifiedWire.getOffsetBounds(mockAttributeSet);
        
        // Assert
        assertNotNull("Bounds should not be null", bounds);
        assertEquals("X should be -10", -10, bounds.getX());
        assertEquals("Y should be 0", 0, bounds.getY());
        assertEquals("Width should be 20", 20, bounds.getWidth());
        assertEquals("Height should be 200", 200, bounds.getHeight());
    }
    
    @Test
    public void testGetOffsetBounds_SouthFacing() {
        // Setup
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_FACING)).thenReturn(Direction.SOUTH);
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_WIDTH)).thenReturn(250);
        
        // Act
        Bounds bounds = unifiedWire.getOffsetBounds(mockAttributeSet);
        
        // Assert
        assertNotNull("Bounds should not be null", bounds);
        assertEquals("Height should be 250", 250, bounds.getHeight());
    }

    // ========== PAINT INSTANCE TESTS ==========
    
    @Test
    public void testPaintInstance_ArrowWire_DrawsBlackWire() throws Exception {
        // Setup
        when(mockInstancePainter.getGraphics()).thenReturn(mockGraphics);
        when(mockInstancePainter.getBounds()).thenReturn(Bounds.create(0, 0, 100, 20));
        when(mockInstancePainter.getAttributeSet()).thenReturn(mockAttributeSet);
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_FACING)).thenReturn(Direction.EAST);
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_BITWIDTH)).thenReturn(BitWidth.create(32));
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_WIRE_TYPE)).thenReturn(UnifiedWire.WIRE_ARROW);
        
        // Act
        unifiedWire.propagate(mockInstanceState); // Setup state
        unifiedWire.paintInstance(mockInstancePainter);
        
        // Assert - verify color was set to BLACK for arrow wire
        verify(mockGraphics, atLeastOnce()).setColor(Color.BLACK);
    }
    
    @Test
    public void testPaintInstance_CallsSetColor() {
        // Setup
        when(mockInstancePainter.getGraphics()).thenReturn(mockGraphics);
        when(mockInstancePainter.getBounds()).thenReturn(Bounds.create(0, 0, 100, 20));
        when(mockInstancePainter.getAttributeSet()).thenReturn(mockAttributeSet);
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_FACING)).thenReturn(Direction.EAST);
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_BITWIDTH)).thenReturn(BitWidth.create(16));
        when(mockAttributeSet.getValue(UnifiedWire.ATTR_WIRE_TYPE)).thenReturn(UnifiedWire.WIRE_ARROW);
        
        // Act
        unifiedWire.paintInstance(mockInstancePainter);
        
        // Assert
        verify(mockGraphics, atLeastOnce()).setColor(any(Color.class));
    }

    // ========== STATIC CONSTANTS TESTS ==========
    
    @Test
    public void testStaticAttributes_NotNull() {
        assertNotNull("WIRE_ARROW should not be null", UnifiedWire.WIRE_ARROW);
        assertNotNull("WIRE_ALU should not be null", UnifiedWire.WIRE_ALU);
        assertNotNull("WIRE_PC should not be null", UnifiedWire.WIRE_PC);
        assertNotNull("WIRE_RBANK should not be null", UnifiedWire.WIRE_RBANK);
        assertNotNull("ATTR_WIRE_TYPE should not be null", UnifiedWire.ATTR_WIRE_TYPE);
        assertNotNull("ATTR_WIDTH should not be null", UnifiedWire.ATTR_WIDTH);
        assertNotNull("ATTR_FACING should not be null", UnifiedWire.ATTR_FACING);
        assertNotNull("ATTR_BITWIDTH should not be null", UnifiedWire.ATTR_BITWIDTH);
    }
    
    @Test
    public void testComponentName_NotNull() {
        assertNotNull("Component name should not be null", UnifiedWire.componentName);
        assertNotNull("Component name toString should not be null", UnifiedWire.getComponentName());
    }

    // ========== INNER CLASSES TESTS ==========
    
    @Test
    public void testUnifiedWirePoker_Instantiation() {
        UnifiedWire.UnifiedWirePoker poker = new UnifiedWire.UnifiedWirePoker();
        assertNotNull("UnifiedWirePoker should not be null", poker);
    }
    
    @Test
    public void testUnifiedWireLogger_GetLogName() {
        UnifiedWire.UnifiedWireLogger logger = new UnifiedWire.UnifiedWireLogger();
        String logName = logger.getLogName(mockInstanceState, null);
        
        assertNotNull("Log name should not be null", logName);
        assertEquals("Log name should be 'Wire Monitor'", "Wire Monitor", logName);
    }
    
    @Test
    public void testUnifiedWireLogger_GetLogValue() {
        UnifiedWire.UnifiedWireLogger logger = new UnifiedWire.UnifiedWireLogger();
        Value testValue = Value.createKnown(32, 100);
        
        when(mockInstanceState.getPortValue(0)).thenReturn(testValue);
        
        Value logValue = logger.getLogValue(mockInstanceState, null);
        
        assertNotNull("Log value should not be null", logValue);
        assertEquals("Log value should match port value", testValue, logValue);
    }
    
    @Test
    public void testUnifiedWireLogger_GetBitWidth() {
        UnifiedWire.UnifiedWireLogger logger = new UnifiedWire.UnifiedWireLogger();
        BitWidth bitWidth = logger.getBitWidth(mockInstanceState, null);
        
        assertNotNull("BitWidth should not be null", bitWidth);
        assertEquals("BitWidth should be 32", 32, bitWidth.getWidth());
    }
}
