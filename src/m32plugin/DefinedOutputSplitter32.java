import java.awt.Graphics;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.StringGetter;

/*
    Este componente separa una entrada de 32 bits. A diferencia del componente
    Splitter, cualquier entrada que esté en estado E o U se devuelve como 0.
*/

public class DefinedOutputSplitter32 extends InstanceFactory {
    static class ComponentName implements StringGetter{
        String name = "Defined Output Splitter";
        
        public ComponentName() {}

        public String toString() {
            return name;
        }
    }

    static ComponentName componentName = new ComponentName();

    public DefinedOutputSplitter32() {
        super("DefinedOutputSplitter", componentName);
        
        setAttributes(new Attribute[] {
            StdAttr.WIDTH
        }, new Object[] {
            BitWidth.create(32)
        });

        setOffsetBounds(Bounds.create(-40, -160, 80, 320));
        
        Port[] ports = new Port[33];

        ports[0] = new Port(-40, 0, Port.INPUT, 32);

        for (int i = 0; i < 32; i++) {
            int yPos = -150 + (i * 10);
            ports[i + 1] = new Port(40, yPos, Port.OUTPUT, 1);
        }
        
        setPorts(ports);
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        Bounds bds = painter.getBounds();

        g.drawRect(bds.getX(), bds.getY(), bds.getWidth(), bds.getHeight());

        g.drawString("D.O. Splitter", bds.getX() + 5, bds.getY() + 15);
        g.drawString("32-bit", bds.getX() + 15, bds.getY() + 30);

        for (int i = 0; i < 32; i++) {
            int yPos = bds.getY() + 160 - 150 + (i * 10);
            g.drawString(String.valueOf(i), bds.getX() + bds.getWidth() - 15, yPos + 3);
        }
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
