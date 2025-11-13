package m32plugin;

import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.AttributeOption;
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

enum WireType {
    ARROW("Arrow Wire"),
    ALU("ALU Wire"),
    PC("Program Counter Wire"),
    RBANK("R-Bank Wire");

    private final String displayName;

    WireType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}


class UnifiedWireName implements StringGetter {
	String name = "Unified Wire";

	public UnifiedWireName() {}

	public String toString() {
		return name;
	}
}

public class UnifiedWire extends InstanceFactory {
	private InstanceState state;

	public static UnifiedWireName componentName = new UnifiedWireName();
	public TunnelValueFetcher tunnelValueFetcher = new TunnelValueFetcher();
	private AuxWire auxMethods = new AuxWire();

	// Create AttributeOption objects for each wire type
	public static final AttributeOption WIRE_ARROW = 
		new AttributeOption(WireType.ARROW, S.getter("Wire Type - Arrow"));
	public static final AttributeOption WIRE_ALU = 
		new AttributeOption(WireType.ALU, S.getter("Wire Type - ALU"));
	public static final AttributeOption WIRE_PC = 
		new AttributeOption(WireType.PC, S.getter("Wire Type - PC"));
	public static final AttributeOption WIRE_RBANK = 
		new AttributeOption(WireType.RBANK, S.getter("Wire Type - RBank"));

	// Attribute for wire type selection (CORRECTED)
	public static final Attribute<AttributeOption> ATTR_WIRE_TYPE =
		Attributes.forOption("wireType", S.getter("Wire Type"),
			new AttributeOption[] { WIRE_ARROW, WIRE_ALU, WIRE_PC, WIRE_RBANK });

	// Add attributes for width and direction
	public static final Attribute<Integer> ATTR_WIDTH =
		Attributes.forIntegerRange("width", 10, 500);

	public static final Attribute<Direction> ATTR_FACING =
		Attributes.forDirection("facing", S.getter("stdFacingAttr"));

	public static final Attribute<BitWidth> ATTR_BITWIDTH =
		Attributes.forBitWidth("bitwidth", S.getter("stdBitWidthIn"));

	public UnifiedWire() {
		super("UnifiedWire", new UnifiedWireName());
		setInstancePoker(UnifiedWirePoker.class);
		setInstanceLogger(UnifiedWireLogger.class);
		setAttributes(
			new Attribute[] { ATTR_WIRE_TYPE, ATTR_WIDTH, ATTR_FACING, ATTR_BITWIDTH },
			new Object[] { WIRE_ARROW, 80, Direction.EAST, BitWidth.create(32) }
		);
	}

	@Override
	protected void configureNewInstance(Instance instance) {
		instance.addAttributeListener();
		updatePorts(instance);
	}

	@Override
	protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
		if (attr == ATTR_WIDTH || attr == ATTR_FACING || attr == ATTR_BITWIDTH || attr == ATTR_WIRE_TYPE) {
			instance.recomputeBounds();
			updatePorts(instance);
		}
	}

	private void updatePorts(Instance instance) {
		AttributeSet attrs = instance.getAttributeSet();
		Direction facing = attrs.getValue(ATTR_FACING);
		int width = attrs.getValue(ATTR_WIDTH);
		AttributeOption wireType = attrs.getValue(ATTR_WIRE_TYPE);
		System.out.println("Hello");
		BitWidth bitWidth = attrs.getValue(ATTR_BITWIDTH);

		// Arrow Wire uses configurable bit width, others use fixed 32-bit
		BitWidth effectiveBitWidth = (wireType == WIRE_ARROW) ? bitWidth : BitWidth.create(32);

		Port[] ports = new Port[2];

		if (facing == Direction.WEST) {
			ports[0] = new Port(width, 0, Port.INPUT, effectiveBitWidth);
			ports[1] = new Port(0, 0, Port.OUTPUT, effectiveBitWidth);
		} else if (facing == Direction.EAST) {
			ports[0] = new Port(0, 0, Port.INPUT, effectiveBitWidth);
			ports[1] = new Port(width, 0, Port.OUTPUT, effectiveBitWidth);
		} else if (facing == Direction.NORTH) {
			ports[0] = new Port(0, width, Port.INPUT, effectiveBitWidth);
			ports[1] = new Port(0, 0, Port.OUTPUT, effectiveBitWidth);
		} else if (facing == Direction.SOUTH) {
			ports[0] = new Port(0, 0, Port.INPUT, effectiveBitWidth);
			ports[1] = new Port(0, width, Port.OUTPUT, effectiveBitWidth);
		}

		instance.setPorts(ports);
	}

	@Override
	public void propagate(InstanceState state) {
		Value input = state.getPortValue(0);
		state.setPort(1, input, 1);
		this.state = state;
	}

	private int getWireThickness(BitWidth bitWidth) {
		int bits = bitWidth.getWidth();
		if (bits <= 1) return 2;		// 1-bit: thin wire
		else if (bits <= 8) return 3;	// 8-bit: medium wire
		else if (bits <= 16) return 4;	// 16-bit: thick wire
		else if (bits <= 32) return 5;	// 32-bit: thicker wire
		else return 6;					// 64-bit+: very thick wire
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		Graphics g = painter.getGraphics();
		Bounds bds = painter.getBounds();
		AttributeSet attrs = painter.getAttributeSet();
		Direction facing = attrs.getValue(ATTR_FACING);
		BitWidth bitWidth = attrs.getValue(ATTR_BITWIDTH);
		AttributeOption wireType = attrs.getValue(ATTR_WIRE_TYPE);
		InstanceState state = this.state;

		Value val;
		if (state == null) {
			val = Value.createKnown(32, 0);
		} else {
			val = state.getPortValue(0);
		}

		Color wireColor = Color.BLACK;

		// Handle wire coloring based on type
		switch (wireType.getValue()) {
			case WireType.ARROW:
				wireColor = Color.BLACK;
				break;
			case WireType.ALU:
				Value aluVal = tunnelValueFetcher.getTunnelValue(painter.getCircuitState(), "OP_ALU");
				wireColor = getColorForALUValue(aluVal);
				break;
			case WireType.PC:
				Value pcVal = tunnelValueFetcher.getTunnelValue(painter.getCircuitState(), "WR_PC");
				if (pcVal == Value.createKnown(1, 1)) {
					wireColor = getColorForDataValue(val);
				}
				break;
			case WireType.RBANK:
				Value rbankVal = tunnelValueFetcher.getTunnelValue(painter.getCircuitState(), "WR_RD");
				if (rbankVal == Value.createKnown(1, 1)) {
					wireColor = getColorForDataValue(val);
				}
				break;
			default:
				wireColor = Color.BLACK;
				break;
		}

		g.setColor(wireColor);
		int centerX = bds.getX() + bds.getWidth() / 2;
		int centerY = bds.getY() + bds.getHeight() / 2;

		// Draw wire based on facing direction
		if (facing == Direction.EAST) {
			drawHorizontalWire(g, bds, wireColor, false, (WireType)wireType.getValue(), bitWidth);
		} else if (facing == Direction.WEST) {
			drawHorizontalWire(g, bds, wireColor, true, (WireType)wireType.getValue(), bitWidth);
		} else if (facing == Direction.NORTH) {
			auxMethods.drawVerticalWire(g, bds, wireColor, true, (wireType == WIRE_ARROW), bitWidth);
		} else if (facing == Direction.SOUTH) {
			auxMethods.drawVerticalWire(g, bds, wireColor, false, (wireType == WIRE_ARROW), bitWidth);
		}

		// Draw value text for non-Arrow wire types
		if (wireType != WIRE_ARROW) {
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
		} else {
			// Arrow wire shows bit width instead
			drawBitWidthText(g, bds, facing, bitWidth);
		}
	}

	private void drawBitWidthText(Graphics g, Bounds bds, Direction facing, BitWidth bitWidth) {
		String text = String.valueOf(bitWidth.getWidth());
		int textX, textY;

		if (facing == Direction.EAST || facing == Direction.WEST) {
			textX = bds.getX() + bds.getWidth() / 2 - 5;
			textY = bds.getY() + bds.getHeight() / 2 - 8;
		} else {
			textX = bds.getX() + bds.getWidth() / 2 + 5;
			textY = bds.getY() + bds.getHeight() / 2;
		}

		g.setColor(Color.DARK_GRAY);
		g.drawString(text, textX, textY);
	}

	private void drawHorizontalWire(Graphics g, Bounds bds, Color wireColor, boolean leftToRight, WireType wireType, BitWidth bitWidth) {
		g.setColor(wireColor);
		int wireY = bds.getY() + bds.getHeight() / 2;
		int wireStartX, wireEndX, arrowX;
		int thickness, halfThickness;

		// Arrow wire has thicker lines based on bit width; others are thin
		if (wireType == WireType.ARROW) {
			thickness = getWireThickness(bitWidth);
			halfThickness = thickness / 2;
			if (leftToRight) {
				wireStartX = bds.getX() + 5;
				wireEndX = bds.getX() + bds.getWidth();
				arrowX = bds.getX();
			} else {
				wireStartX = bds.getX();
				wireEndX = bds.getX() + bds.getWidth() - 5;
				arrowX = bds.getX() + bds.getWidth();
			}
		} else {
			// Other wire types use standard thickness
			thickness = 4;
			halfThickness = 2;
			if (leftToRight) {
				wireStartX = bds.getX() + 15;
				wireEndX = bds.getX() + bds.getWidth() - 5;
				arrowX = bds.getX() + 8;
			} else {
				wireStartX = bds.getX() + 5;
				wireEndX = bds.getX() + bds.getWidth() - 15;
				arrowX = bds.getX() + bds.getWidth() - 8;
			}
		}

		g.fillRect(wireStartX, wireY - halfThickness, wireEndX - wireStartX, thickness);
		drawHorizontalArrow(g, arrowX, wireY, leftToRight, wireType, bitWidth);
	}

	private void drawHorizontalArrow(Graphics g, int arrowX, int arrowY, boolean pointingLeft, WireType wireType, BitWidth bitWidth) {
		int baseArrowSize = 12;
		int arrowSize = (wireType == WIRE_ARROW.getValue()) ? 
			baseArrowSize + Math.min(6, getWireThickness(bitWidth) - 2) : baseArrowSize;

		int[] arrowXPoints, arrowYPoints;

		if (pointingLeft) {
			arrowXPoints = new int[] { arrowX, arrowX + arrowSize, arrowX + arrowSize };
		} else {
			arrowXPoints = new int[] { arrowX, arrowX - arrowSize, arrowX - arrowSize };
		}

		arrowYPoints = new int[] { arrowY, arrowY - arrowSize/2, arrowY + arrowSize/2 };
		g.fillPolygon(arrowXPoints, arrowYPoints, 3);
	}

	private Color getColorForALUValue(Value val) {
		if (val == Value.createKnown(4, 0)) return Color.RED;
		else if (val == Value.createKnown(4, 1)) return Color.ORANGE;
		else if (val == Value.createKnown(4, 2)) {
			float[] hsbVals = Color.RGBtoHSB(0xFF, 0xB7, 0x03, null);
			return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
		}
		else if (val == Value.createKnown(4, 3)) return Color.YELLOW;
		else if (val == Value.createKnown(4, 4)) {
			float[] hsbVals = Color.RGBtoHSB(0x80, 0xED, 0x99, null);
			return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
		}
		else if (val == Value.createKnown(4, 5)) return Color.GREEN;
		else if (val == Value.createKnown(4, 6)) {
			float[] hsbVals = Color.RGBtoHSB(0x11, 0x8A, 0xB2, null);
			return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
		}
		else if (val == Value.createKnown(4, 7)) return Color.CYAN;
		else if (val == Value.createKnown(4, 8)) {
			float[] hsbVals = Color.RGBtoHSB(0x4C, 0xC9, 0xF0, null);
			return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
		}
		else if (val == Value.createKnown(4, 9)) return Color.BLUE;
		else if (val == Value.createKnown(4, 10)) {
			float[] hsbVals = Color.RGBtoHSB(0x3A, 0x0C, 0xA3, null);
			return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
		}
		else if (val == Value.createKnown(4, 11)) {
			float[] hsbVals = Color.RGBtoHSB(0x72, 0x09, 0xB7, null);
			return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
		}
		else if (val == Value.createKnown(4, 12)) return Color.MAGENTA;
		else if (val == Value.createKnown(4, 13)) return Color.PINK;
		else if (val == Value.createKnown(4, 14)) {
			float[] hsbVals = Color.RGBtoHSB(0x8D, 0x55, 0x24, null);
			return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
		}
		else if (val == Value.createKnown(4, 15)) {
			float[] hsbVals = Color.RGBtoHSB(0x7D, 0x85, 0x97, null);
			return Color.getHSBColor(hsbVals[0], hsbVals[1], hsbVals[2]);
		}
		return Color.GRAY;
	}

	private Color getColorForDataValue(Value val) {
		if (!val.isFullyDefined()) {
			return Color.GRAY;
		}
		return Color.MAGENTA;
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

	public static class UnifiedWirePoker extends InstancePoker {
		@Override
		public void mousePressed(InstanceState state, MouseEvent e) {
			// Optional: Add interaction functionality
		}
	}

	public static class UnifiedWireLogger extends InstanceLogger {
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
			return BitWidth.create(32);
		}
	}
}
