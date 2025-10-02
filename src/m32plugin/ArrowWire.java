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
    
    // Add attributes for width and direction
    public static final Attribute<Integer> ATTR_WIDTH = 
        Attributes.forIntegerRange("width", 10, 200);
    public static final Attribute<Direction> ATTR_FACING = 
        Attributes.forDirection("facing", S.getter("stdFacingAttr"));
    public static final Attribute<BitWidth> ATTR_BITWIDTH =
        Attributes.forBitWidth("bitwidth", S.getter("plaBitWidthIn"));

    private BitWidth bitWidth = BitWidth.create(32);

    public ArrowWire() {
        super("ArrowWire", new ArrowWireName());
        setInstancePoker(ArrowWirePoker.class);
        setInstanceLogger(ArrowWireLogger.class);
        setAttributes(
            new Attribute[] { ATTR_WIDTH, ATTR_FACING, ATTR_BITWIDTH },
            new Object[] { 80, Direction.EAST, bitWidth }
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
        } else if (attr == ATTR_BITWIDTH) {
            int value = instance.getAttributeSet()
                                   .getValue(ATTR_BITWIDTH)
                                   .getWidth();
            this.bitWidth = BitWidth.create(value);
        }
        updatePorts(instance);
    }
    
    private void updatePorts(Instance instance) {
        AttributeSet attrs = instance.getAttributeSet();
        Direction facing = attrs.getValue(ATTR_FACING);
        int width = attrs.getValue(ATTR_WIDTH);
        
        Port[] ports = new Port[2];
        
        if (facing == Direction.WEST) {
            ports[0] = new Port(width, 0, Port.INPUT, BitWidth.create(bitWidth.getWidth()));
            ports[1] = new Port(0, 0, Port.OUTPUT, BitWidth.create(bitWidth.getWidth()));
        } else if (facing == Direction.EAST) {
            ports[0] = new Port(0, 0, Port.INPUT, BitWidth.create(bitWidth.getWidth()));
            ports[1] = new Port(width, 0, Port.OUTPUT, BitWidth.create(bitWidth.getWidth()));
        } else if (facing == Direction.NORTH) {
            ports[0] = new Port(0, width, Port.INPUT, BitWidth.create(bitWidth.getWidth()));
            ports[1] = new Port(0, 0, Port.OUTPUT, BitWidth.create(bitWidth.getWidth()));
        } else if (facing == Direction.SOUTH) {
            ports[0] = new Port(0, 0, Port.INPUT, BitWidth.create(bitWidth.getWidth()));
            ports[1] = new Port(0, width, Port.OUTPUT, BitWidth.create(bitWidth.getWidth()));
        }
        
        instance.setPorts(ports);
    }
    
    @Override
    public void propagate(InstanceState state) {
        Value input = state.getPortValue(0);
        state.setPort(1, input, 1);
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        Bounds bds = painter.getBounds();
        AttributeSet attrs = painter.getAttributeSet();
        Direction facing = attrs.getValue(ATTR_FACING);

        g.setColor(Color.BLACK);
        
        if (facing == Direction.EAST) {
            drawHorizontalWire(g, bds, Color.BLACK, false);
        } else if (facing == Direction.WEST) {
            drawHorizontalWire(g, bds, Color.BLACK, true);
        } else if (facing == Direction.NORTH) {
            drawVerticalWire(g, bds, Color.BLACK, true);
        } else if (facing == Direction.SOUTH) {
            drawVerticalWire(g, bds, Color.BLACK, false);
        }
    }
    
    private void drawHorizontalWire(Graphics g, Bounds bds, Color wireColor, boolean leftToRight) {
        g.setColor(wireColor);
        int wireY = bds.getY() + bds.getHeight() / 2;
        int wireStartX, wireEndX, arrowX;
        
        if (leftToRight) {
            wireStartX = bds.getX() + 5;
            wireEndX = bds.getX() + bds.getWidth();
            arrowX = bds.getX();
        } else {
            wireStartX = bds.getX();
            wireEndX = bds.getX() + bds.getWidth() - 5;
            arrowX = bds.getX() + bds.getWidth();
        }
        
        g.fillRect(wireStartX, wireY - 2, wireEndX - wireStartX, 4);
        
        drawHorizontalArrow(g, arrowX, wireY, leftToRight);
    }
    
    private void drawVerticalWire(Graphics g, Bounds bds, Color wireColor, boolean upToDown) {
        g.setColor(wireColor);
        int wireX = bds.getX() + bds.getWidth() / 2;
        int wireStartY, wireEndY, arrowY;
        
        if (upToDown) {
            wireStartY = bds.getY() + 5;
            wireEndY = bds.getY() + bds.getHeight();
            arrowY = bds.getY();
        } else {
            wireStartY = bds.getY();
            wireEndY = bds.getY() + bds.getHeight() - 5;
            arrowY = bds.getY() + bds.getHeight();
        }
        
        g.fillRect(wireX - 2, wireStartY, 4, wireEndY - wireStartY);
        
        drawVerticalArrow(g, wireX, arrowY, upToDown);
    }
    
    private void drawHorizontalArrow(Graphics g, int arrowX, int arrowY, boolean pointingLeft) {
        int arrowSize = 12;
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
    
    private void drawVerticalArrow(Graphics g, int arrowX, int arrowY, boolean pointingUp) {
        int arrowSize = 12;
        int[] arrowXPoints, arrowYPoints;
        
        arrowXPoints = new int[] {
            arrowX,
            arrowX - arrowSize/2,
            arrowX + arrowSize/2
        };
        
        if (pointingUp) {
            arrowYPoints = new int[] {
                arrowY,
                arrowY + arrowSize,
                arrowY + arrowSize
            };
        } else {
            arrowYPoints = new int[] {
                arrowY,
                arrowY - arrowSize,
                arrowY - arrowSize
            };
        }
        
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