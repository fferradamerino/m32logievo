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

class ALUWireName implements StringGetter{
	String name = "ALU Wire";
	
	public ALUWireName() {}

	public String toString() {
		return name;
	}
}

public class ALUWire extends InstanceFactory {
    private InstanceState state;
    public static ALUWireName componentName = new ALUWireName();
    public TunnelValueFetcher tunnelValueFetcher = new TunnelValueFetcher();

    private AuxWire auxMethods = new AuxWire();
    
    // Add attributes for width and direction
    public static final Attribute<Integer> ATTR_WIDTH = 
        Attributes.forIntegerRange("width", 60, 500);
    public static final Attribute<Direction> ATTR_FACING = 
        Attributes.forDirection("facing", S.getter("stdFacingAttr"));

    public ALUWire() {
        super("ALUWire", new ALUWireName());
        setInstancePoker(ALUWirePoker.class);
        setInstanceLogger(ALUWireLogger.class);
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
        
        if (facing == Direction.WEST) {
            ports[0] = new Port(0, 0, Port.INPUT, BitWidth.create(32));
            ports[1] = new Port(width, 0, Port.OUTPUT, BitWidth.create(32));
        } else if (facing == Direction.EAST) {
            ports[0] = new Port(0, 0, Port.INPUT, BitWidth.create(32));
            ports[1] = new Port(width, 0, Port.OUTPUT, BitWidth.create(32));
        } else if (facing == Direction.NORTH) {
            ports[0] = new Port(0, width, Port.INPUT, BitWidth.create(32));
            ports[1] = new Port(0, 0, Port.OUTPUT, BitWidth.create(32));
        } else if (facing == Direction.SOUTH) {
            ports[0] = new Port(0, 0, Port.INPUT, BitWidth.create(32));
            ports[1] = new Port(0, width, Port.OUTPUT, BitWidth.create(32));
        }
        
        instance.setPorts(ports);
    }
    
    @Override
    public void propagate(InstanceState state) {
        Value input = state.getPortValue(0);
        state.setPort(1, input, 1);
        this.state = state;
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        Bounds bds = painter.getBounds();
        AttributeSet attrs = painter.getAttributeSet();
        Direction facing = attrs.getValue(ATTR_FACING);

        InstanceState state = this.state;
        Value val;
        if (state == null) {
            val = Value.createKnown(32, 0);
        } else {
            val = state.getPortValue(0);
        }

        Color wireColor = Color.BLACK;

        Value currentVal = tunnelValueFetcher.getTunnelValue(
            painter.getCircuitState(), "OP_ALU");

        wireColor = getColorForValue(currentVal);

        g.setColor(wireColor);
        
        int centerX = bds.getX() + bds.getWidth() / 2;
        int centerY = bds.getY() + bds.getHeight() / 2;
        
        if (facing == Direction.EAST) {
            drawHorizontalWire(g, bds, wireColor, false);
        } else if (facing == Direction.WEST) {
            drawHorizontalWire(g, bds, wireColor, true);
        } else if (facing == Direction.NORTH) {
            auxMethods.drawVerticalWire(g, bds, wireColor, true, false, null);
        } else if (facing == Direction.SOUTH) {
            auxMethods.drawVerticalWire(g, bds, wireColor, false, false, null);
        }
        
        g.setColor(Color.BLACK);
        String valueStr = val.isFullyDefined() ? val.toHexString() : "X";
        
        int textX = centerX;
        int textY = centerY;
        
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            textX += 15;
        } else {
            textY += 15;
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
        
        drawHorizontalArrow(g, arrowX, wireY, leftToRight);
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
    
    private Color getColorForValue(Value val) {
        if (val == Value.createKnown(4, 0)) {
            return Color.RED;
        } else if (val == Value.createKnown(4, 1)) {
            return Color.ORANGE;
        } else if (val == Value.createKnown(4, 2)) {
            float[] hsbVals = Color.RGBtoHSB(0xFF, 0xB7, 0x03, null);
            return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
        } else if (val == Value.createKnown(4, 3)) {
            return Color.YELLOW;
        } else if (val == Value.createKnown(4, 4)) {
            float[] hsbVals = Color.RGBtoHSB(0x80, 0xED, 0x99, null);
            return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
        } else if (val == Value.createKnown(4, 5)) {
            return Color.GREEN;
        } else if (val == Value.createKnown(4, 6)) {
            float[] hsbVals = Color.RGBtoHSB(0x11, 0x8A, 0xB2, null);
            return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
        } else if (val == Value.createKnown(4, 7)) {
            return Color.CYAN;
        } else if (val == Value.createKnown(4, 8)) {
            float[] hsbVals = Color.RGBtoHSB(0x4C, 0xC9, 0xF0, null);
            return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
        } else if (val == Value.createKnown(4, 9)) {
            return Color.BLUE;
        } else if (val == Value.createKnown(4, 10)) {
            float[] hsbVals = Color.RGBtoHSB(0x3A, 0x0C, 0xA3, null);
            return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
        } else if (val == Value.createKnown(4, 11)) {
            float[] hsbVals = Color.RGBtoHSB(0x72, 0x09, 0xB7, null);
            return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
        } else if (val == Value.createKnown(4, 12)) {
            return Color.MAGENTA;
        } else if (val == Value.createKnown(4, 13)) {
            return Color.PINK;
        } else if (val == Value.createKnown(4, 14)) {
            float[] hsbVals = Color.RGBtoHSB(0x8D, 0x55, 0x24, null);
            return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
        } else if (val == Value.createKnown(4, 15)) {
            float[] hsbVals = Color.RGBtoHSB(0x7D, 0x85, 0x97, null);
            return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
        }

        return Color.GRAY;
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
    
    public static class ALUWirePoker extends InstancePoker {
        @Override
        public void mousePressed(InstanceState state, MouseEvent e) {
            // Optional: Add interaction functionality
        }
    }
    
    public static class ALUWireLogger extends InstanceLogger {
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