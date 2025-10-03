import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.Attributes;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.util.StringGetter;

import static com.cburch.logisim.std.Strings.S;

class QuadByteRamName implements StringGetter {
    String name = "Quad Byte RAM";
    
    public QuadByteRamName() {}

    public String toString() {
        return name;
    }
}

public class QuadByteRam extends InstanceFactory {
    private static final int ADDR = 0;
    private static final int DATA_IN = 1;
    private static final int ENABLE = 2;
    private static final int LOAD = 3;
    private static final int CLK = 4;

    private static final int OUT0 = 5;
    private static final int OUT1 = 6;
    private static final int OUT2 = 7;
    private static final int OUT3 = 8;

    public InstanceState instanceState;

    // Custom attribute for file selection with GUI integration
    private static final Attribute<String> FILE_ATTR = Attributes.forString("file", S.getter("Memory File"));

    public static QuadByteRamName componentName = new QuadByteRamName();

    public QuadByteRam() {
        super("QuadByte RAM", componentName);
        setOffsetBounds(Bounds.create(0, 0, 120, 100));

        setPorts(new Port[] {
            new Port(0, 10, Port.INPUT, BitWidth.create(8)),   // Address input
            new Port(0, 30, Port.INPUT, BitWidth.create(8)),   // Data in
            new Port(0, 50, Port.INPUT, 1),                    // Enable
            new Port(0, 70, Port.INPUT, 1),                    // Load
            new Port(0, 90, Port.INPUT, 1),                   // Clock
            new Port(120, 20, Port.OUTPUT, BitWidth.create(8)),  // OUT0
            new Port(120, 40, Port.OUTPUT, BitWidth.create(8)),  // OUT1
            new Port(120, 60, Port.OUTPUT, BitWidth.create(8)),  // OUT2
            new Port(120, 80, Port.OUTPUT, BitWidth.create(8)),  // OUT3
        });

        setAttributes(new Attribute[] { FILE_ATTR }, new Object[] { "" });
    }

    @Override
    protected void configureNewInstance(Instance instance) {
        instance.addAttributeListener();
    }

    @Override
    public void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
        if (attr == FILE_ATTR) {
            String filename = instance.getAttributeValue(FILE_ATTR);
            QuadByteRamData ramData = QuadByteRamData.get(this.instanceState);
            
            if (filename != null && !filename.isEmpty()) {
                try {
                    File f = new File(filename);
                    if (f.exists() && f.isFile()) {
                        byte[] data = Files.readAllBytes(f.toPath());
                        int len = Math.min(data.length, ramData.memory.length);
                        // Clear memory first
                        java.util.Arrays.fill(ramData.memory, (byte)0);
                        // Copy new data
                        System.arraycopy(data, 0, ramData.memory, 0, len);
                        
                        // Notify user of successful load
                        JOptionPane.showMessageDialog(null, 
                            "Loaded " + len + " bytes from " + f.getName(),
                            "Memory Loaded", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "File not found: " + filename,
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, 
                        "Error reading file: " + e.getMessage(),
                        "File Error", 
                        JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                } catch (SecurityException e) {
                    JOptionPane.showMessageDialog(null, 
                        "Permission denied reading file: " + filename,
                        "Security Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    @Override
    public void propagate(InstanceState state) {
        Value addrVal = state.getPortValue(ADDR);
        Value dinVal  = state.getPortValue(DATA_IN);
        Value enVal   = state.getPortValue(ENABLE);
        Value loadVal = state.getPortValue(LOAD);
        Value clkVal  = state.getPortValue(CLK);

        this.instanceState = state;

        QuadByteRamData data = QuadByteRamData.get(state);
        
        if (!addrVal.isFullyDefined()) {
            // Set all outputs to unknown if address is not defined
            state.setPort(OUT0, Value.createUnknown(BitWidth.create(8)), 0);
            state.setPort(OUT1, Value.createUnknown(BitWidth.create(8)), 0);
            state.setPort(OUT2, Value.createUnknown(BitWidth.create(8)), 0);
            state.setPort(OUT3, Value.createUnknown(BitWidth.create(8)), 0);
            return;
        }

        int addr = Integer.parseInt(addrVal.toDecimalString(false)) & 0xFF;

        // Write on rising edge if LOAD = 1
        if (data.lastClock == Value.FALSE && clkVal == Value.TRUE && loadVal == Value.TRUE) {
            if (dinVal.isFullyDefined()) {
                data.memory[addr] = (byte) Integer.parseInt(dinVal.toDecimalString(false));
            }
        }
        data.lastClock = clkVal;

        // Read if ENABLE = 1
        if (enVal == Value.TRUE) {
            state.setPort(OUT0, Value.createKnown(BitWidth.create(8), data.memory[addr & 0xFF] & 0xFF), 0);
            state.setPort(OUT1, Value.createKnown(BitWidth.create(8), data.memory[(addr + 1) & 0xFF] & 0xFF), 0);
            state.setPort(OUT2, Value.createKnown(BitWidth.create(8), data.memory[(addr + 2) & 0xFF] & 0xFF), 0);
            state.setPort(OUT3, Value.createKnown(BitWidth.create(8), data.memory[(addr + 3) & 0xFF] & 0xFF), 0);
        } else {
            // High impedance if not enabled
            state.setPort(OUT0, Value.createUnknown(BitWidth.create(8)), 0);
            state.setPort(OUT1, Value.createUnknown(BitWidth.create(8)), 0);
            state.setPort(OUT2, Value.createUnknown(BitWidth.create(8)), 0);
            state.setPort(OUT3, Value.createUnknown(BitWidth.create(8)), 0);
        }
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        painter.drawBounds();
        painter.drawLabel();
        painter.drawPort(ADDR, "Addr", Direction.WEST);
        painter.drawPort(DATA_IN, "Din", Direction.WEST);
        painter.drawPort(ENABLE, "En", Direction.WEST);
        painter.drawPort(LOAD, "Ld", Direction.WEST);
        painter.drawPort(CLK, "Clk", Direction.WEST);

        painter.drawPort(OUT0, "Q0", Direction.EAST);
        painter.drawPort(OUT1, "Q1", Direction.EAST);
        painter.drawPort(OUT2, "Q2", Direction.EAST);
        painter.drawPort(OUT3, "Q3", Direction.EAST);
    }
}

// Properly implement InstanceData for Logisim compatibility
class QuadByteRamData implements InstanceData {
    public byte[] memory;
    public Value lastClock;
    public boolean fileLoaded;
    
    public QuadByteRamData() {
        memory = new byte[256];
        lastClock = Value.FALSE;
        fileLoaded = false;
    }
    
    @Override
    public Object clone() {
        try {
            QuadByteRamData copy = (QuadByteRamData) super.clone();
            copy.memory = memory.clone();
            copy.lastClock = lastClock;
            copy.fileLoaded = fileLoaded;
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone should be supported");
        }
    }
    
    public static QuadByteRamData get(InstanceState state) {
        QuadByteRamData data = (QuadByteRamData) state.getData();
        if (data == null) {
            data = new QuadByteRamData();
            state.setData(data);
        }
        return data;
    }
}

// Custom file attribute editor with file picker dialog
class FileAttributeEditor extends javax.swing.JPanel {
    private javax.swing.JTextField textField;
    private javax.swing.JButton browseButton;
    private String currentValue;
    
    public FileAttributeEditor(String initialValue) {
        this.currentValue = initialValue == null ? "" : initialValue;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
        
        textField = new javax.swing.JTextField(currentValue);
        textField.setEditable(false);
        textField.setPreferredSize(new java.awt.Dimension(200, 25));
        
        browseButton = new javax.swing.JButton("Browse...");
        browseButton.setPreferredSize(new java.awt.Dimension(80, 25));
        browseButton.addActionListener(e -> browseForFile());
        
        add(textField, java.awt.BorderLayout.CENTER);
        add(browseButton, java.awt.BorderLayout.EAST);
    }
    
    private void browseForFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Memory File");
        
        // Set file filters
        FileNameExtensionFilter binFilter = new FileNameExtensionFilter("Binary Files (*.bin)", "bin");
        FileNameExtensionFilter datFilter = new FileNameExtensionFilter("Data Files (*.dat)", "dat");
        FileNameExtensionFilter allFilter = new FileNameExtensionFilter("All Files", "*");
        
        fileChooser.addChoosableFileFilter(binFilter);
        fileChooser.addChoosableFileFilter(datFilter);
        fileChooser.addChoosableFileFilter(allFilter);
        fileChooser.setFileFilter(binFilter);
        
        // Set initial directory if we have a current file
        if (!currentValue.isEmpty()) {
            File currentFile = new File(currentValue);
            if (currentFile.getParentFile() != null && currentFile.getParentFile().exists()) {
                fileChooser.setCurrentDirectory(currentFile.getParentFile());
            }
        }
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            currentValue = selectedFile.getAbsolutePath();
            textField.setText(selectedFile.getName());
            
            // Fire property change to notify Logisim
            firePropertyChange("value", null, currentValue);
        }
    }
    
    public String getValue() {
        return currentValue;
    }
}