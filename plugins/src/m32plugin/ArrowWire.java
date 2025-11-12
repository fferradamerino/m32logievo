package m32plugin;

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

import static com.cburch.logisim.std.Strings.S;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.cburch.logisim.util.StringGetter;

class ArrowWireName implements StringGetter{
	String name = "Arrow Wire";
	
	public ArrowWireName() {}

	public String toString() {
		return name;
	}
}

public class ArrowWire extends InstanceFactory {
    public static ArrowWireName componentName = new ArrowWireName();
    public TunnelValueFetcher tunnelValueFetcher = new TunnelValueFetcher();

    private AuxWire auxMethods = new AuxWire();
    
    // Add attributes for width and direction
    public static final Attribute<Integer> ATTR_WIDTH = 
        Attributes.forIntegerRange("width", 10, 200);
    public static final Attribute<Direction> ATTR_FACING = 
        Attributes.forDirection("facing", S.getter("stdFacingAttr"));
    public static final Attribute<BitWidth> ATTR_BITWIDTH =
        Attributes.forBitWidth("bitwidth", S.getter("stdBitWidthIn"));

    public ArrowWire() {
        super("ArrowWire", new ArrowWireName());
        setInstancePoker(ArrowWirePoker.class);
        setInstanceLogger(ArrowWireLogger.class);
        setAttributes(
            new Attribute[] { ATTR_WIDTH, ATTR_FACING, ATTR_BITWIDTH },
            new Object[] { 80, Direction.EAST, BitWidth.create(32) }
        );
    }
    
    @Override
    protected void configureNewInstance(Instance instance) {
        instance.addAttributeListener();
        updatePorts(instance);
    }
    
    @Override
    protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
        if (attr == ATTR_WIDTH || attr == ATTR_FACING || attr == ATTR_BITWIDTH) {
            instance.recomputeBounds();
            updatePorts(instance);
        }
    }
    
    private void updatePorts(Instance instance) {
        AttributeSet attrs = instance.getAttributeSet();
        Direction facing = attrs.getValue(ATTR_FACING);
        int width = attrs.getValue(ATTR_WIDTH);
        BitWidth bitWidth = attrs.getValue(ATTR_BITWIDTH);
        
        Port[] ports = new Port[2];
        
        if (facing == Direction.WEST) {
            ports[0] = new Port(width, 0, Port.INPUT, bitWidth);
            ports[1] = new Port(0, 0, Port.OUTPUT, bitWidth);
        } else if (facing == Direction.EAST) {
            ports[0] = new Port(0, 0, Port.INPUT, bitWidth);
            ports[1] = new Port(width, 0, Port.OUTPUT, bitWidth);
        } else if (facing == Direction.NORTH) {
            ports[0] = new Port(0, width, Port.INPUT, bitWidth);
            ports[1] = new Port(0, 0, Port.OUTPUT, bitWidth);
        } else if (facing == Direction.SOUTH) {
            ports[0] = new Port(0, 0, Port.INPUT, bitWidth);
            ports[1] = new Port(0, width, Port.OUTPUT, bitWidth);
        }
        
        instance.setPorts(ports);
    }
    
    @Override
    public void propagate(InstanceState state) {
        Value input = state.getPortValue(0);
        state.setPort(1, input, 1);
    }

    private int getWireThickness(BitWidth bitWidth) {
        int bits = bitWidth.getWidth();
        if (bits <= 1) return 2;      // 1-bit: thin wire
        else if (bits <= 8) return 3; // 8-bit: medium wire
        else if (bits <= 16) return 4; // 16-bit: thick wire
        else if (bits <= 32) return 5; // 32-bit: thicker wire
        else return 6;                 // 64-bit+: very thick wire
    }

    private void drawBitWidthText(Graphics g, Bounds bds, Direction facing, BitWidth bitWidth) {
        String text = String.valueOf(bitWidth.getWidth());
        int textX, textY;
        
        // Calculate text position based on direction
        if (facing == Direction.EAST || facing == Direction.WEST) {
            // Horizontal wire - position text slightly above the wire
            textX = bds.getX() + bds.getWidth() / 2 - 5;
            textY = bds.getY() + bds.getHeight() / 2 - 8;
        } else {
            // Vertical wire - position text slightly to the right of the wire
            textX = bds.getX() + bds.getWidth() / 2 + 5;
            textY = bds.getY() + bds.getHeight() / 2;
        }
        
        // Set a contrasting color for better visibility
        g.setColor(Color.DARK_GRAY);
        
        // Draw the text
        g.drawString(text, textX, textY);
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        Bounds bds = painter.getBounds();
        AttributeSet attrs = painter.getAttributeSet();
        Direction facing = attrs.getValue(ATTR_FACING);
        BitWidth bitWidth = attrs.getValue(ATTR_BITWIDTH);

        g.setColor(Color.BLACK);
        
        if (facing == Direction.EAST) {
            drawHorizontalWire(g, bds, Color.BLACK, false, bitWidth);
        } else if (facing == Direction.WEST) {
            drawHorizontalWire(g, bds, Color.BLACK, true, bitWidth);
        } else if (facing == Direction.NORTH) {
            auxMethods.drawVerticalWire(g, bds, Color.BLACK, true, true, bitWidth);
        } else if (facing == Direction.SOUTH) {
            auxMethods.drawVerticalWire(g, bds, Color.BLACK, false, true, bitWidth);
        }
        
        // Draw bit width text near the middle of the wire
        drawBitWidthText(g, bds, facing, bitWidth);
    }
    
    private void drawHorizontalWire(Graphics g, Bounds bds, Color wireColor, boolean leftToRight, BitWidth bitWidth) {
        g.setColor(wireColor);
        int wireY = bds.getY() + bds.getHeight() / 2;
        int wireStartX, wireEndX, arrowX;
        int thickness = getWireThickness(bitWidth);
        int halfThickness = thickness / 2;
        
        if (leftToRight) {
            wireStartX = bds.getX() + 5;
            wireEndX = bds.getX() + bds.getWidth();
            arrowX = bds.getX();
        } else {
            wireStartX = bds.getX();
            wireEndX = bds.getX() + bds.getWidth() - 5;
            arrowX = bds.getX() + bds.getWidth();
        }
        
        g.fillRect(wireStartX, wireY - halfThickness, wireEndX - wireStartX, thickness);
        
        drawHorizontalArrow(g, arrowX, wireY, leftToRight, bitWidth);
    }
    
    private void drawHorizontalArrow(Graphics g, int arrowX, int arrowY, boolean pointingLeft, BitWidth bitWidth) {
        int baseArrowSize = 12;
        int arrowSize = baseArrowSize + Math.min(6, getWireThickness(bitWidth) - 2); // Scale arrow with thickness
        int[] arrowXPoints, arrowYPoints;
        
        if (pointingLeft) {
            arrowXPoints = new int[] {
                arrowX,
                arrowX + arrowSize,
                arrowX + arrowSize
            };
        } else {
            arrowXPoints = new int[] {
                arrowX,
                arrowX - arrowSize,
                arrowX - arrowSize
            };
        }
        
        arrowYPoints = new int[] {
            arrowY,
            arrowY - arrowSize/2,
            arrowY + arrowSize/2
        };
        
        g.fillPolygon(arrowXPoints, arrowYPoints, 3);
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
            return Bounds.create(-10, 0, 20, width);
        } else if (facing == Direction.SOUTH) {
            return Bounds.create(-10, 0, 20, width);
        } else {
            return Bounds.create(0, -10, width, 20);
        }
    }
    
    public static class ArrowWirePoker extends InstancePoker {
        @Override
        public void mousePressed(InstanceState state, MouseEvent e) {
            // Optional: Add interaction functionality
        }
    }
    
    public static class ArrowWireLogger extends InstanceLogger {
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