import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.Attributes;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstanceLogger;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstancePoker;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.GraphicsUtil;

import static com.cburch.logisim.std.Strings.S;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.cburch.logisim.util.StringGetter;

class ColoredWireName implements StringGetter{
	String name = "Colored Wire";
	
	public ColoredWireName() {}

	public String toString() {
		return name;
	}
}

public class ColoredWireMonitor extends InstanceFactory {
    private InstanceState state;
    public static ColoredWireName componentName = new ColoredWireName();
    
    // Add attributes for width and direction
    public static final Attribute<Integer> ATTR_WIDTH = 
        Attributes.forIntegerRange("width", 60, 200);
    public static final Attribute<Direction> ATTR_FACING = 
        Attributes.forDirection("facing", S.getter("stdFacingAttr"));

    public ColoredWireMonitor() {
        super("ColoredWireMonitor", new ColoredWireName());
        setInstancePoker(ColoredWirePoker.class);
        setInstanceLogger(ColoredWireLogger.class);
        setAttributes(
            new Attribute[] { ATTR_WIDTH, ATTR_FACING },
            new Object[] { 80, Direction.EAST }
        );
    }
    
    @Override
    protected void configureNewInstance(Instance instance) {
        instance.addAttributeListener();
        updatePorts(instance);
    }
    
    @Override
    protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
        if (attr == ATTR_WIDTH || attr == ATTR_FACING) {
            instance.recomputeBounds();
            updatePorts(instance);
        }
    }
    
    private void updatePorts(Instance instance) {
        AttributeSet attrs = instance.getAttributeSet();
        Direction facing = attrs.getValue(ATTR_FACING);
        int width = attrs.getValue(ATTR_WIDTH);
        
        Port[] ports = new Port[2];
        
        // Calculate port positions based on direction
        if (facing == Direction.WEST) {
            ports[0] = new Port(0, 0, Port.INPUT, BitWidth.create(8));
            ports[1] = new Port(width, 0, Port.OUTPUT, BitWidth.create(8));
        } else if (facing == Direction.EAST) {
            ports[0] = new Port(0, 0, Port.INPUT, BitWidth.create(8));
            ports[1] = new Port(width, 0, Port.OUTPUT, BitWidth.create(8));
        } else if (facing == Direction.NORTH) {
            ports[0] = new Port(0, width, Port.INPUT, BitWidth.create(8));
            ports[1] = new Port(0, 0, Port.OUTPUT, BitWidth.create(8));
        } else if (facing == Direction.SOUTH) {
            ports[0] = new Port(0, 0, Port.INPUT, BitWidth.create(8));
            ports[1] = new Port(0, width, Port.OUTPUT, BitWidth.create(8));
        }
        
        instance.setPorts(ports);
    }
    
    @Override
    public void propagate(InstanceState state) {
        // Simply pass the input to the output
        Value input = state.getPortValue(0);
        state.setPort(1, input, 1);
        this.state = state; // To later paint the component
    }
    
    @Override
    public void paintInstance(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        Bounds bds = painter.getBounds();
        AttributeSet attrs = painter.getAttributeSet();
        Direction facing = attrs.getValue(ATTR_FACING);
        
        // Get the current value to determine color
        InstanceState state = this.state;
        Value val;
        if (state == null) {
            val = Value.createKnown(8, 0);
        } else {
            val = state.getPortValue(0);
        }
        
        // Determine color based on value
        Color wireColor = getColorForValue(val);

        // Draw colored line representing the wire
        g.setColor(wireColor);
        
        int centerX = bds.getX() + bds.getWidth() / 2;
        int centerY = bds.getY() + bds.getHeight() / 2;
        
        if (facing == Direction.EAST) {
            drawHorizontalWire(g, bds, wireColor, false);
        } else if (facing == Direction.WEST) {
            drawHorizontalWire(g, bds, wireColor, true);
        } else if (facing == Direction.NORTH) {
            drawVerticalWire(g, bds, wireColor, true);
        } else if (facing == Direction.SOUTH) {
            drawVerticalWire(g, bds, wireColor, false);
        }
        
        // Draw value as text
        g.setColor(Color.BLACK);
        String valueStr = val.isFullyDefined() ? val.toHexString() : "X";
        
        // Position text based on orientation
        int textX = centerX;
        int textY = centerY;
        
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            textX += 15; // Offset to the side for vertical wires
        } else {
            textY += 15; // Offset below for horizontal wires
        }
        
        GraphicsUtil.drawCenteredText(g, valueStr, textX, textY);
    }
    
    private void drawHorizontalWire(Graphics g, Bounds bds, Color wireColor, boolean leftToRight) {
        g.setColor(wireColor);
        int wireY = bds.getY() + bds.getHeight() / 2;
        int wireStartX, wireEndX, arrowX;
        
        if (leftToRight) {
            wireStartX = bds.getX() + 15;
            wireEndX = bds.getX() + bds.getWidth() - 5;
            arrowX = bds.getX() + 8;
        } else {
            wireStartX = bds.getX() + 5;
            wireEndX = bds.getX() + bds.getWidth() - 15;
            arrowX = bds.getX() + bds.getWidth() - 8;
        }
        
        g.fillRect(wireStartX, wireY - 2, wireEndX - wireStartX, 4);
        
        // Draw arrow
        drawHorizontalArrow(g, arrowX, wireY, leftToRight);
    }
    
    private void drawVerticalWire(Graphics g, Bounds bds, Color wireColor, boolean upToDown) {
        g.setColor(wireColor);
        int wireX = bds.getX() + bds.getWidth() / 2;
        int wireStartY, wireEndY, arrowY;
        
        if (upToDown) {
            wireStartY = bds.getY() + 15;
            wireEndY = bds.getY() + bds.getHeight() - 5;
            arrowY = bds.getY() + 8;
        } else {
            wireStartY = bds.getY() + 5;
            wireEndY = bds.getY() + bds.getHeight() - 15;
            arrowY = bds.getY() + bds.getHeight() - 8;
        }
        
        g.fillRect(wireX - 2, wireStartY, 4, wireEndY - wireStartY);
        
        // Draw arrow
        drawVerticalArrow(g, wireX, arrowY, upToDown);
    }
    
    private void drawHorizontalArrow(Graphics g, int arrowX, int arrowY, boolean pointingLeft) {
        int arrowSize = 12;
        int[] arrowXPoints, arrowYPoints;
        
        if (pointingLeft) {
            arrowXPoints = new int[] {
                arrowX,                    // Arrow tip
                arrowX + arrowSize,        // Right point of arrow base
                arrowX + arrowSize         // Right point of arrow base
            };
        } else {
            arrowXPoints = new int[] {
                arrowX,                    // Arrow tip
                arrowX - arrowSize,        // Left point of arrow base
                arrowX - arrowSize         // Left point of arrow base
            };
        }
        
        arrowYPoints = new int[] {
            arrowY,                    // Arrow tip
            arrowY - arrowSize/2,      // Top of arrow base
            arrowY + arrowSize/2       // Bottom of arrow base
        };
        
        g.fillPolygon(arrowXPoints, arrowYPoints, 3);
    }
    
    private void drawVerticalArrow(Graphics g, int arrowX, int arrowY, boolean pointingUp) {
        int arrowSize = 12;
        int[] arrowXPoints, arrowYPoints;
        
        arrowXPoints = new int[] {
            arrowX,                    // Arrow tip
            arrowX - arrowSize/2,      // Left point of arrow base
            arrowX + arrowSize/2       // Right point of arrow base
        };
        
        if (pointingUp) {
            arrowYPoints = new int[] {
                arrowY,                    // Arrow tip
                arrowY + arrowSize,        // Bottom point of arrow base
                arrowY + arrowSize         // Bottom point of arrow base
            };
        } else {
            arrowYPoints = new int[] {
                arrowY,                    // Arrow tip
                arrowY - arrowSize,        // Top point of arrow base
                arrowY - arrowSize         // Top point of arrow base
            };
        }
        
        g.fillPolygon(arrowXPoints, arrowYPoints, 3);
    }
    
    private Color getColorForValue(Value val) {
        if (!val.isFullyDefined()) {
            return Color.GRAY;
        }
        
        int intVal = Integer.parseInt(val.toDecimalString(false));
        
        // Extended color coding with more colors
        if (intVal == 0) {
            return Color.BLACK;
        } else if (intVal < 32) {
            return Color.BLUE;
        } else if (intVal < 64) {
            return new Color(0, 206, 209); // Turquoise
        } else if (intVal < 96) {
            return Color.GREEN;
        } else if (intVal < 128) {
            return new Color(50, 205, 50); // Lime Green
        } else if (intVal < 160) {
            return Color.YELLOW;
        } else if (intVal < 192) {
            return Color.ORANGE;
        } else if (intVal < 224) {
            return Color.RED;
        } else if (intVal < 240) {
            return Color.MAGENTA;
        } else if (intVal < 250) {
            return new Color(138, 43, 226); // Blue Violet
        } else if (intVal < 255) {
            return new Color(255, 20, 147); // Deep Pink
        } else {
            return Color.WHITE;
        }
    }
    
    @Override
    public Bounds getOffsetBounds(AttributeSet attrs) {
        Direction facing = attrs.getValue(ATTR_FACING);
        int width = attrs.getValue(ATTR_WIDTH);
        
        if (facing == Direction.EAST) {
            return Bounds.create(0, -10, width, 20);
        } else if (facing == Direction.WEST) {
            return Bounds.create(0, -10, width, 20);
        } else if (facing == Direction.NORTH) {
            return Bounds.create(0, -10, width, 20);
        } else if (facing == Direction.SOUTH) {
            return Bounds.create(-10, 0, 20, width);
        } else {
            return Bounds.create(0, -10, width, 20);
        }
    }
    
    public static class ColoredWirePoker extends InstancePoker {
        @Override
        public void mousePressed(InstanceState state, MouseEvent e) {
            // Optional: Add interaction functionality
        }
    }
    
    public static class ColoredWireLogger extends InstanceLogger {
        @Override
        public String getLogName(InstanceState state, Object option) {
            return "Wire Monitor";
        }
        
        @Override
        public Value getLogValue(InstanceState state, Object option) {
            return state.getPortValue(0);
        }

        @Override
        public BitWidth getBitWidth(InstanceState state, Object option) {
            return BitWidth.create(8);
        }
    }
}