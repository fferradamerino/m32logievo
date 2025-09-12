package m32plugin;

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

        setOffsetBounds(Bounds.create(-20, -10, 40, 20));
        setPorts(new Port[] {
            new Port(0, 0, Port.INPUT, 32),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
            new Port(20, 0, Port.OUTPUT, 1),
        });
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        Bounds bds = painter.getBounds();
        g.drawRect(bds.getX(), bds.getY(), bds.getWidth(), bds.getHeight());
        g.drawString("D.O. Splitter", bds.getX() + 2, bds.getY() + 12);
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
        state.setPort(1, getVal(input, 0), 1);
        state.setPort(2, getVal(input, 1), 1);
        state.setPort(3, getVal(input, 2), 1);
        state.setPort(4, getVal(input, 3), 1);
        state.setPort(5, getVal(input, 4), 1);
        state.setPort(6, getVal(input, 5), 1);
        state.setPort(7, getVal(input, 6), 1);
        state.setPort(8, getVal(input, 7), 1);
        state.setPort(9, getVal(input, 8), 1);
        state.setPort(10, getVal(input, 9), 1);
        state.setPort(11, getVal(input, 10), 1);
        state.setPort(12, getVal(input, 11), 1);
        state.setPort(13, getVal(input, 12), 1);
        state.setPort(14, getVal(input, 13), 1);
        state.setPort(15, getVal(input, 14), 1);
        state.setPort(16, getVal(input, 15), 1);
        state.setPort(17, getVal(input, 16), 1);
        state.setPort(18, getVal(input, 17), 1);
        state.setPort(19, getVal(input, 18), 1);
        state.setPort(20, getVal(input, 19), 1);
        state.setPort(21, getVal(input, 20), 1);
        state.setPort(22, getVal(input, 21), 1);
        state.setPort(23, getVal(input, 22), 1);
        state.setPort(24, getVal(input, 23), 1);
        state.setPort(25, getVal(input, 24), 1);
        state.setPort(26, getVal(input, 25), 1);
        state.setPort(27, getVal(input, 26), 1);
        state.setPort(28, getVal(input, 27), 1);
        state.setPort(29, getVal(input, 28), 1);
        state.setPort(30, getVal(input, 29), 1);
        state.setPort(31, getVal(input, 30), 1);
        state.setPort(32, getVal(input, 31), 1);
    }
}
