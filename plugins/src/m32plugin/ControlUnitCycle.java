package m32plugin;

import java.awt.Color;
import java.awt.Graphics;
import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.StringGetter;

class ControlUnitCycleName implements StringGetter{
	String name = "Control Unit Cycle";
	
	public ControlUnitCycleName() {}

	public String toString() {
		return name;
	}
}

public class ControlUnitCycle extends InstanceFactory {
    public static ControlUnitCycleName componentName = new ControlUnitCycleName();

    public ControlUnitCycle() {
        super("ControlUnitCycle");
        
        setAttributes(new Attribute[] {}, new Object[] {});
        
        setOffsetBounds(Bounds.create(0, 0, 200, 30));

        setPorts(new Port[] {
            new Port(100, 30, Port.INPUT, BitWidth.create(8)) // ADDR
        });
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        Instance instance = painter.getInstance();
        Graphics g = painter.getGraphics();

        painter.drawBounds();

        painter.drawPorts();

        Value addrValue = painter.getPortValue(0);
        
        String displayText = getDisplayText(addrValue);
        
        Bounds bds = instance.getBounds();
        int x = bds.getX() + bds.getWidth() / 2;
        int y = bds.getY() + bds.getHeight() / 2;
        
        g.setColor(Color.BLACK);
        painter.getGraphics().drawString(displayText, x - g.getFontMetrics().stringWidth(displayText) / 2, y);
    }

    @Override
    public void propagate(InstanceState state) {
        // No hay lógica acá
    }

    private String getDisplayText(Value addrValue) {
        if (!addrValue.isFullyDefined()) {
            return "Dirección desconocida";
        }
        
        int addr = Integer.parseInt(addrValue.toDecimalString(false));
        
        if (addr == 0) {
            return "FETCH 1";
        } else if (addr == 1 || addr == 2) {
            return "FETCH 2";
        } else if (addr == 3 || addr == 4) {
            return "DECOD";
        } else {
            return "EXECUTE";
        }
    }
}