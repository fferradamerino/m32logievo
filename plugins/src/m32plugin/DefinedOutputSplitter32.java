import java.awt.Graphics;
import java.awt.Color;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.StdAttr;
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
        
        int x = bds.getX();
        int y = bds.getY();
        int width = bds.getWidth();
        int height = bds.getHeight();

        // Draw a trapezoidal shape like the standard splitter
        int[] xPoints = {x, x + 30, x + 30, x};
        int[] yPoints = {y, y + 40, y + height - 40, y + height};
        
        // Fill background
        Color oldColor = g.getColor();
        g.setColor(Color.WHITE);
        g.fillPolygon(xPoints, yPoints, 4);
        
        // Draw outline
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 4);
        
        // Draw lines from the trapezoid to each output
        for (int i = 0; i < 32; i++) {
            int yPos = y + 10 + (i * 10);
            g.drawLine(x + 30, yPos, x + width, yPos);
        }
        
        // Draw bit numbers on the right side
        g.setColor(Color.BLUE);
        for (int i = 0; i < 32; i++) {
            int yPos = y + 10 + (i * 10);
            String label = String.valueOf(31 - i); // MSB at top
            // Position text slightly to the left of the right edge
            int labelWidth = g.getFontMetrics().stringWidth(label);
            g.drawString(label, x + width - labelWidth - 2, yPos + 4);
        }
        
        // Draw "32" label on the input side
        g.setColor(Color.BLUE);
        g.drawString("32", x + 2, y + height / 2 + 4);
        
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