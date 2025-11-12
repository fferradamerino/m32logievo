package m32plugin;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;

import java.awt.Graphics;
import java.awt.Color;

public class AuxWire {
    private int getWireThickness(BitWidth bitWidth) {
        int bits = bitWidth.getWidth();
        if (bits <= 1) return 2;      // 1-bit: thin wire
        else if (bits <= 8) return 3; // 8-bit: medium wire
        else if (bits <= 16) return 4; // 16-bit: thick wire
        else if (bits <= 32) return 5; // 32-bit: thicker wire
        else return 6;                 // 64-bit+: very thick wire
    }

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

    public void drawVerticalWire(Graphics g, Bounds bds, Color wireColor, boolean upToDown, boolean thick, BitWidth bitWidth) {
        g.setColor(wireColor);
        int wireX = bds.getX() + bds.getWidth() / 2;
        int wireStartY, wireEndY, arrowY;
        int thickness = 4;
        int halfThickness = 2;

        if (thick) {
            thickness = getWireThickness(bitWidth);
            halfThickness = thickness / 2;
        }
        
        if (upToDown) {
            wireStartY = bds.getY();
            wireEndY = bds.getY() + bds.getHeight() - 5;
            arrowY = bds.getY();
        } else {
            wireStartY = bds.getY();
            wireEndY = bds.getY() + bds.getHeight() - 5;
            arrowY = bds.getY() + bds.getHeight();
        }
        
        g.fillRect(wireX - halfThickness, wireStartY, thickness, wireEndY - wireStartY);
        
        drawVerticalArrow(g, wireX, arrowY, upToDown);
    }
}