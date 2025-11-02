import com.cburch.logisim.data.Bounds;

import java.awt.Graphics;
import java.awt.Color;

public class AuxWire {
    public void drawVerticalArrow(Graphics g, int arrowX, int arrowY, boolean pointingUp) {
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

    public void drawVerticalWire(Graphics g, Bounds bds, Color wireColor, boolean upToDown) {
        g.setColor(wireColor);
        int wireX = bds.getX() + bds.getWidth() / 2;
        int wireStartY, wireEndY, arrowY;
        
        if (upToDown) {
            wireStartY = bds.getY();
            wireEndY = bds.getY() + bds.getHeight() - 5;
            arrowY = bds.getY();
        } else {
            wireStartY = bds.getY();
            wireEndY = bds.getY() + bds.getHeight() - 5;
            arrowY = bds.getY() + bds.getHeight();
        }
        
        g.fillRect(wireX - 2, wireStartY, 4, wireEndY - wireStartY);
        
        drawVerticalArrow(g, wireX, arrowY, upToDown);
    }
}