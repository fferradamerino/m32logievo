package m32plugin;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.awt.Graphics;
import java.awt.Color;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import m32plugin.AuxWire;

public class AuxWireTest {
    
    private AuxWire auxWire;
    private Graphics graphics;
    private Bounds bounds;
    
    @Before
    public void setUp() {
        auxWire = new AuxWire();
        graphics = mock(Graphics.class);
        bounds = mock(Bounds.class);
    }
    
    @Test
    public void testDrawVerticalArrowPointingUp() {
        auxWire.drawVerticalArrow(graphics, 100, 50, true);
        
        ArgumentCaptor<int[]> xCaptor = ArgumentCaptor.forClass(int[].class);
        ArgumentCaptor<int[]> yCaptor = ArgumentCaptor.forClass(int[].class);
        ArgumentCaptor<Integer> nPointsCaptor = ArgumentCaptor.forClass(Integer.class);
        
        verify(graphics).fillPolygon(xCaptor.capture(), yCaptor.capture(), nPointsCaptor.capture());
        
        int[] xPoints = xCaptor.getValue();
        int[] yPoints = yCaptor.getValue();
        
        assertEquals(3, xPoints.length);
        assertEquals(100, xPoints[0]);
        assertEquals(94, xPoints[1]);
        assertEquals(106, xPoints[2]);
        
        assertEquals(50, yPoints[0]);
        assertEquals(62, yPoints[1]);
        assertEquals(62, yPoints[2]);
        
        assertEquals(Integer.valueOf(3), nPointsCaptor.getValue());
    }
    
    @Test
    public void testDrawVerticalArrowPointingDown() {
        auxWire.drawVerticalArrow(graphics, 100, 50, false);
        
        ArgumentCaptor<int[]> xCaptor = ArgumentCaptor.forClass(int[].class);
        ArgumentCaptor<int[]> yCaptor = ArgumentCaptor.forClass(int[].class);
        
        verify(graphics).fillPolygon(xCaptor.capture(), yCaptor.capture(), eq(3));
        
        int[] yPoints = yCaptor.getValue();
        
        assertEquals(50, yPoints[0]);
        assertEquals(38, yPoints[1]);
        assertEquals(38, yPoints[2]);
    }
    
    @Test
    public void testDrawVerticalWireUpToDown() {
        when(bounds.getX()).thenReturn(0);
        when(bounds.getY()).thenReturn(0);
        when(bounds.getWidth()).thenReturn(100);
        when(bounds.getHeight()).thenReturn(200);
        
        BitWidth bitWidth = BitWidth.create(1);
        
        auxWire.drawVerticalWire(graphics, bounds, Color.RED, true, false, bitWidth);
        
        verify(graphics).setColor(Color.RED);
        verify(graphics).fillRect(48, 0, 4, 195);
        verify(graphics).fillPolygon(any(int[].class), any(int[].class), eq(3));
    }
    
    @Test
    public void testDrawVerticalWireDownToUp() {
        when(bounds.getX()).thenReturn(0);
        when(bounds.getY()).thenReturn(0);
        when(bounds.getWidth()).thenReturn(100);
        when(bounds.getHeight()).thenReturn(200);
        
        BitWidth bitWidth = BitWidth.create(1);
        
        auxWire.drawVerticalWire(graphics, bounds, Color.BLUE, false, false, bitWidth);
        
        verify(graphics).setColor(Color.BLUE);
        verify(graphics).fillRect(48, 0, 4, 195);
    }
    
    @Test
    public void testDrawVerticalWireWithThickWire1Bit() {
        when(bounds.getX()).thenReturn(0);
        when(bounds.getY()).thenReturn(0);
        when(bounds.getWidth()).thenReturn(100);
        when(bounds.getHeight()).thenReturn(200);
        
        BitWidth bitWidth = BitWidth.create(1);
        
        auxWire.drawVerticalWire(graphics, bounds, Color.GREEN, true, true, bitWidth);
        
        verify(graphics).fillRect(49, 0, 2, 195);
    }
    
    @Test
    public void testDrawVerticalWireWithThickWire8Bit() {
        when(bounds.getX()).thenReturn(0);
        when(bounds.getY()).thenReturn(0);
        when(bounds.getWidth()).thenReturn(100);
        when(bounds.getHeight()).thenReturn(200);
        
        BitWidth bitWidth = BitWidth.create(8);
        
        auxWire.drawVerticalWire(graphics, bounds, Color.YELLOW, true, true, bitWidth);
        
        verify(graphics).fillRect(49, 0, 3, 195);
    }
    
    @Test
    public void testDrawVerticalWireWithThickWire16Bit() {
        when(bounds.getX()).thenReturn(0);
        when(bounds.getY()).thenReturn(0);
        when(bounds.getWidth()).thenReturn(100);
        when(bounds.getHeight()).thenReturn(200);
        
        BitWidth bitWidth = BitWidth.create(16);
        
        auxWire.drawVerticalWire(graphics, bounds, Color.CYAN, true, true, bitWidth);
        
        verify(graphics).fillRect(48, 0, 4, 195);
    }
    
    @Test
    public void testDrawVerticalWireWithThickWire32Bit() {
        when(bounds.getX()).thenReturn(0);
        when(bounds.getY()).thenReturn(0);
        when(bounds.getWidth()).thenReturn(100);
        when(bounds.getHeight()).thenReturn(200);
        
        BitWidth bitWidth = BitWidth.create(32);
        
        auxWire.drawVerticalWire(graphics, bounds, Color.MAGENTA, true, true, bitWidth);
        
        verify(graphics).fillRect(48, 0, 5, 195);
    }
    
    @Test
    public void testDrawVerticalWireWithThickWire64Bit() {
        when(bounds.getX()).thenReturn(0);
        when(bounds.getY()).thenReturn(0);
        when(bounds.getWidth()).thenReturn(100);
        when(bounds.getHeight()).thenReturn(200);
        
        BitWidth bitWidth = BitWidth.create(64);
        
        auxWire.drawVerticalWire(graphics, bounds, Color.ORANGE, true, true, bitWidth);
        
        verify(graphics).fillRect(47, 0, 6, 195);
    }
}