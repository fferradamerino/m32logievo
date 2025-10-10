import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;

/*
    Este componente separa una entrada de 32 bits. A diferencia del componente
    Splitter, cualquier entrada que esté en estado E o U se devuelve como 0.
*/

public class DefinedOutputSplitter32 extends InstanceFactory {
    static ComponentName componentName = new ComponentName();

    public DefinedOutputSplitter32() {
        super("DefinedOutputSplitter32", componentName);
        
        setAttributes(new Attribute[] {
            StdAttr.FACING,
            StdAttr.WIDTH
        }, new Object[] {
            Direction.EAST,
            BitWidth.create(32)
        });

        setFacingAttribute(StdAttr.FACING);
    }
    
    @Override
    public Bounds getOffsetBounds(com.cburch.logisim.data.AttributeSet attrs) {
        Direction facing = attrs.getValue(StdAttr.FACING);
        
        if (facing == Direction.EAST) {
            return Bounds.create(-40, -160, 80, 320);
        } else if (facing == Direction.WEST) {
            return Bounds.create(-40, -160, 80, 320);
        } else if (facing == Direction.NORTH) {
            return Bounds.create(-160, -40, 320, 80);
        } else { // SOUTH
            return Bounds.create(-160, -40, 320, 80);
        }
    }
    
    @Override
    protected void configureNewInstance(com.cburch.logisim.instance.Instance instance) {
        instance.addAttributeListener();
        updatePorts(instance);
    }
    
    @Override
    protected void instanceAttributeChanged(com.cburch.logisim.instance.Instance instance, Attribute<?> attr) {
        if (attr == StdAttr.FACING) {
            instance.recomputeBounds();
            updatePorts(instance);
        }
    }
    
    private void updatePorts(Instance instance) {
        Direction facing = instance.getAttributeValue(StdAttr.FACING);
        Port[] ports = new Port[33];
        
        if (facing == Direction.EAST) {
            ports[0] = new Port(-40, 0, Port.INPUT, 32);
            for (int i = 0; i < 32; i++) {
                int yPos = -150 + (i * 10);
                ports[i + 1] = new Port(40, yPos, Port.OUTPUT, 1);
            }
        } else if (facing == Direction.WEST) {
            ports[0] = new Port(40, 0, Port.INPUT, 32);
            for (int i = 0; i < 32; i++) {
                int yPos = -150 + (i * 10);
                ports[i + 1] = new Port(-40, yPos, Port.OUTPUT, 1);
            }
        } else if (facing == Direction.NORTH) {
            ports[0] = new Port(0, 40, Port.INPUT, 32);
            for (int i = 0; i < 32; i++) {
                int xPos = -150 + (i * 10);
                ports[i + 1] = new Port(xPos, -40, Port.OUTPUT, 1);
            }
        } else { // SOUTH
            ports[0] = new Port(0, -40, Port.INPUT, 32);
            for (int i = 0; i < 32; i++) {
                int xPos = -150 + (i * 10);
                ports[i + 1] = new Port(xPos, 40, Port.OUTPUT, 1);
            }
        }
        
        instance.setPorts(ports);
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        Bounds bds = painter.getBounds();
        Direction facing = painter.getAttributeValue(StdAttr.FACING);
        
        Graphics2D g2 = (Graphics2D) g.create();
        
        // Translate to center and rotate
        int cx = bds.getX() + bds.getWidth() / 2;
        int cy = bds.getY() + bds.getHeight() / 2;
        
        g2.translate(cx, cy);
        
        if (facing == Direction.NORTH) {
            g2.rotate(Math.toRadians(-90));
        } else if (facing == Direction.SOUTH) {
            g2.rotate(Math.toRadians(90));
        } else if (facing == Direction.WEST) {
            g2.rotate(Math.toRadians(180));
        }
        
        // Draw centered at origin
        int width = 80;
        int height = 320;
        int x = -width / 2;
        int y = -height / 2;

        // Draw a trapezoidal shape like the standard splitter
        int[] xPoints = {x, x + 30, x + 30, x};
        int[] yPoints = {y, y + 40, y + height - 40, y + height};
        
        // Fill background
        Color oldColor = g2.getColor();
        g2.setColor(Color.WHITE);
        g2.fillPolygon(xPoints, yPoints, 4);
        
        // Draw outline
        g2.setColor(Color.BLACK);
        g2.drawPolygon(xPoints, yPoints, 4);
        
        // Draw lines from the trapezoid to each output
        for (int i = 0; i < 32; i++) {
            int yPos = y + 10 + (i * 10);
            g2.drawLine(x + 30, yPos, x + width, yPos);
        }
        
        // Draw bit numbers on the right side
        g2.setColor(Color.BLUE);
        for (int i = 0; i < 32; i++) {
            int yPos = y + 10 + (i * 10);
            String label = String.valueOf(31 - i); // MSB at top
            int labelWidth = g2.getFontMetrics().stringWidth(label);
            g2.drawString(label, x + width - labelWidth - 2, yPos + 4);
        }
        
        // Draw "32" label on the input side
        g2.setColor(Color.BLUE);
        g2.drawString("32", x + 2, y + height / 2 + 4);
        
        g2.dispose();
        
        g.setColor(oldColor);
        painter.drawPorts();
    }
    
    private Value getVal(Value input, int n) {
        if (
            input.get(n) == Value.ERROR ||
            input.get(n) == Value.NIL ||
            input.get(n) == Value.UNKNOWN
        ) {
            return Value.FALSE;
        }

        return input.get(n);
    }

    @Override
    public void propagate(InstanceState state) {
        Value input = state.getPortValue(0);

        for (int i = 0; i < 32; i++) {
            state.setPort(i + 1, getVal(input, i), 1);
        }
    }
}