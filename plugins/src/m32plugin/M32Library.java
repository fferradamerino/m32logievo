package m32plugin;

import com.cburch.logisim.tools.FactoryDescription;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;
import com.cburch.logisim.comp.ComponentDrawContext;
import com.cburch.logisim.gui.main.Canvas;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.instance.InstancePainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.Icon;
import javax.imageio.ImageIO;

public class M32Library extends Library {
    private List<Tool> tools = null;

    public static final String _ID = "M32Library";

    private static final FactoryDescription[] DESCRIPTIONS = {
        new FactoryDescription(DefinedOutputSplitter32.class, DefinedOutputSplitter32.componentName),
        new FactoryDescription(QuadByteRam.class, QuadByteRam.componentName),
        new FactoryDescription(ControlUnitCycle.class, ControlUnitCycle.componentName),
        new FactoryDescription(UnifiedWire.class, UnifiedWire.componentName),
        new FactoryDescription(ControlUnitCycleMain.class, ControlUnitCycleMain.componentName),
        new FactoryDescription(RamStick.class, RamStick.componentName)
    };

    public M32Library() {}

    @Override
    public String getDisplayName() {
        return "Biblioteca de componentes M32";
    }

    public List<Tool> getTools() {
        if (tools == null) {
            List<Tool> toolList = new ArrayList<>(FactoryDescription.getTools(M32Library.class, DESCRIPTIONS));
            toolList.add(new ScreenshotTool());
            tools = toolList;
        }
        return tools;
    }

    private static class ScreenshotTool extends Tool {
        
        public ScreenshotTool() {
        }
        
        @Override
        public String getName() {
            return "Screenshot";
        }
        
        @Override
        public String getDisplayName() {
            return "Captura de pantalla";
        }
        
        @Override
        public String getDescription() {
            return "Captura una imagen del circuito actual";
        }
        
        @Override
        public void paintIcon(ComponentDrawContext c, int x, int y) {
            Graphics g = c.getGraphics();
            g.setColor(Color.BLACK);
            // Draw camera body
            g.drawRect(x + 2, y + 4, 12, 9);
            // Draw viewfinder
            g.fillRect(x + 6, y + 2, 4, 2);
            // Draw lens
            g.fillOval(x + 6, y + 7, 4, 4);
        }
        
        @Override
        public void draw(Canvas canvas, ComponentDrawContext context) {
            // Not used for this tool
        }
        
        @Override
        public void select(Canvas canvas) {
            // When tool is selected, immediately take screenshot
            takeScreenshot(canvas);
            // Deselect the tool after taking screenshot
            if (canvas != null && canvas.getProject() != null) {
                canvas.getProject().setTool(null);
            }
        }
        
        @Override
        public void deselect(Canvas canvas) {
            // Nothing to do on deselect
        }
        
        @Override
        public void mousePressed(Canvas canvas, Graphics g, MouseEvent e) {
            // Not needed - screenshot happens on select
        }
        
        @Override
        public void mouseDragged(Canvas canvas, Graphics g, MouseEvent e) {
            // Not needed
        }
        
        @Override
        public void mouseReleased(Canvas canvas, Graphics g, MouseEvent e) {
            // Not needed
        }
        
        @Override
        public void mouseMoved(Canvas canvas, Graphics g, MouseEvent e) {
            // Not needed
        }
        
        @Override
        public void keyTyped(Canvas canvas, java.awt.event.KeyEvent e) {
            // Not needed
        }
        
        @Override
        public void keyPressed(Canvas canvas, java.awt.event.KeyEvent e) {
            // Not needed
        }
        
        @Override
        public void keyReleased(Canvas canvas, java.awt.event.KeyEvent e) {
            // Not needed
        }
        
        @Override
        public Cursor getCursor() {
            return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        }
        
        @Override
        public AttributeSet getAttributeSet() {
            return null;
        }
        
        @Override
        public boolean isAllDefaultValues(AttributeSet attrs, com.cburch.logisim.LogisimVersion ver) {
            return true;
        }
        
        private void takeScreenshot(Canvas canvas) {
            if (canvas == null) {
                JOptionPane.showMessageDialog(null, 
                    "No hay canvas disponible para capturar",
                    "Error de captura", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            SwingUtilities.invokeLater(() -> {
                try {
                    // Get canvas dimensions
                    int width = canvas.getWidth();
                    int height = canvas.getHeight();
                    
                    if (width <= 0 || height <= 0) {
                        JOptionPane.showMessageDialog(null,
                            "El canvas tiene dimensiones inválidas",
                            "Error de captura",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Create buffered image
                    BufferedImage image = new BufferedImage(
                        width, height, BufferedImage.TYPE_INT_RGB);
                    
                    // Paint canvas to image
                    Graphics2D g2d = image.createGraphics();
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(0, 0, width, height);
                    canvas.paint(g2d);
                    g2d.dispose();
                    
                    // Show save dialog
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Guardar captura de pantalla");
                    fileChooser.setSelectedFile(new File("captura_circuito.png"));
                    fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                        @Override
                        public boolean accept(File f) {
                            return f.isDirectory() || 
                                   f.getName().toLowerCase().endsWith(".png");
                        }
                        
                        @Override
                        public String getDescription() {
                            return "Imágenes PNG (*.png)";
                        }
                    });
                    
                    int result = fileChooser.showSaveDialog(canvas);
                    
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        
                        // Add .png extension if not present
                        if (!file.getName().toLowerCase().endsWith(".png")) {
                            file = new File(file.getAbsolutePath() + ".png");
                        }
                        
                        // Save image
                        ImageIO.write(image, "PNG", file);
                        
                        JOptionPane.showMessageDialog(canvas,
                            "Captura guardada en:\n" + file.getAbsolutePath(),
                            "Captura guardada",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                        "Error al guardar la captura: " + e.getMessage(),
                        "Error de captura",
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            });
        }
    }
}