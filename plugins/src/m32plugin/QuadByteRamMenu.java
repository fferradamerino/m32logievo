package m32plugin;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.tools.MenuExtender;

public class QuadByteRamMenu implements ActionListener, MenuExtender {
    public Project proj;
    public Frame frame;

    public Instance instance;

    private CircuitState circState;
    private JMenuItem load;
    private JMenuItem dump;

    QuadByteRamMenu(Instance instance) {
        this.instance = instance;
    }

    private JMenuItem createItem(boolean enabled, String label) {
        final var ret = new JMenuItem(label);
        ret.setEnabled(enabled);
        ret.addActionListener(this);
        return ret;
    }

    @Override
    public void configureMenu(JPopupMenu menu, Project proj) {
        this.proj = proj;
        this.frame = proj.getFrame();
        this.circState = proj.getCircuitState();

        var enabled = circState != null;
        load = createItem(enabled, "Cargar imagen binaria");

        dump = createItem(enabled, "Volcar memoria a archivo");
        menu.addSeparator();
        menu.add(load);
        menu.add(dump);
    }

    private void doLoad() {
        if (circState == null) {
            JOptionPane.showMessageDialog(frame,
                "No circuit state available",
                "Load Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Binary Image into RAM");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Optional: Add file filter for binary files
        // fileChooser.setFileFilter(new FileNameExtensionFilter("Binary files", "bin", "dat", "hex"));
        
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null && selectedFile.exists()) {
                loadFileIntoRam(selectedFile.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(frame,
                    "Selected file does not exist or cannot be accessed",
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadFileIntoRam(String filepath) {
        try {
            // Get the instance state for this component
            InstanceState instanceState = circState.getInstanceState(instance);
            
            // Get or create RAM data
            Object data = instanceState.getData();
            if (data instanceof RamData32) {
                RamData32 ramData = (RamData32) data;
                
                // Load the file
                boolean success = ramData.loadFromFile(filepath);
                
                if (success) {
                    ramData.setFilename(filepath);
                    ramData.setFileLoaded(true);
                    
                    // Force the component to repaint and propagate
                    instanceState.fireInvalidated();
                    
                    JOptionPane.showMessageDialog(frame,
                        "Successfully loaded binary image from:\n" + filepath,
                        "Load Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame,
                        "Failed to load binary image from:\n" + filepath,
                        "Load Failed",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Create new RAM data if it doesn't exist
                int size = instance.getAttributeValue(QuadByteRam.ATTR_SIZE);
                int dataBits = instance.getAttributeValue(QuadByteRam.ATTR_DATA_BITS).getWidth();
                RamData32 ramData = new RamData32(size, dataBits);
                
                boolean success = ramData.loadFromFile(filepath);
                if (success) {
                    ramData.setFilename(filepath);
                    ramData.setFileLoaded(true);
                    instanceState.setData(ramData);
                    instanceState.fireInvalidated();
                    
                    JOptionPane.showMessageDialog(frame,
                        "Successfully loaded binary image from:\n" + filepath,
                        "Load Successful",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame,
                        "Failed to load binary image from:\n" + filepath,
                        "Load Failed",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                "Error loading file: " + e.getMessage(),
                "Load Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void doDump() {
        if (circState == null) {
            JOptionPane.showMessageDialog(frame, "No circuit state available", "Dump Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Dump RAM Contents to Binary File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showSaveDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null) {
                dumpRamToFile(selectedFile.getAbsolutePath());
            }
        }
    }

    private void dumpRamToFile(String filepath) {
        try {
            InstanceState instanceState = circState.getInstanceState(instance);
            Object data = instanceState.getData();
            if (data instanceof RamData32) {
                RamData32 ramData = (RamData32) data;
                boolean success = ramData.dumpToFile(filepath);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Successfully dumped RAM contents to:\n" + filepath, "Dump Successful", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to dump RAM contents to:\n" + filepath, "Dump Failed", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No RAM data available to dump", "Dump Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error dumping RAM: " + e.getMessage(), "Dump Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == load) {
            doLoad();
        } else if (src == dump) {
            doDump();
        }
    }
}