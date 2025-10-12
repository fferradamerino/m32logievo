import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.Attributes;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.tools.MenuExtender;
import com.cburch.logisim.util.StringGetter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class RamData32 implements InstanceData, Cloneable {
    private int[] data;
    private String filename;
    private Value lastClk;
    private boolean fileLoaded;
    private int size;
    private int dataBits;
    private int waitCycleCount;

    public RamData32(int size, int dataBits) {
        this.size = size;
        this.dataBits = dataBits;
        this.data = new int[size];
        // Initialize with zeros
        for (int i = 0; i < size; i++) {
            data[i] = 0;
        }
        lastClk = Value.FALSE;
        fileLoaded = false;
        waitCycleCount = 0;
    }

    public int getData(int addr) {
        if (addr >= 0 && addr < data.length) {
            return this.data[addr];
        }
        return 0;
    }

    public void setData(int addr, int val) {
        if (addr >= 0 && addr < data.length) {
            this.data[addr] = val;
        }
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Value getLastClk() {
        return this.lastClk;
    }

    public void setLastClk(Value clk) {
        this.lastClk = clk;
    }

    public boolean isFileLoaded() {
        return this.fileLoaded;
    }

    public void setFileLoaded(boolean loaded) {
        this.fileLoaded = loaded;
    }

    public int getWaitCycleCount() {
        return this.waitCycleCount;
    }

    public void setWaitCycleCount(int count) {
        this.waitCycleCount = count;
    }

    public void decrementWaitCycle() {
        if (this.waitCycleCount > 0) {
            this.waitCycleCount--;
        }
    }

    public void incrementWaitCycle() {
        this.waitCycleCount++;
    }

    public boolean loadFromFile(String filepath) {
        if (filepath == null || filepath.trim().isEmpty()) {
            return false;
        }

        File file = new File(filepath);
        if (!file.exists() || !file.canRead()) {
            System.err.println("Error: Cannot read file: " + filepath);
            return false;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            int bytesPerLocation = (dataBits + 7) / 8;
            int bytesRead = 0;
            int value;
            int location = 0;
            int currentValue = 0;
            int byteIndex = 0;
            
            while ((value = fis.read()) != -1 && location < data.length) {
                currentValue |= (value & 0xFF) << (byteIndex * 8);
                byteIndex++;
                bytesRead++;
                
                if (byteIndex >= bytesPerLocation) {
                    data[location] = currentValue;
                    location++;
                    currentValue = 0;
                    byteIndex = 0;
                }
            }
            
            // Handle remaining bytes for last location
            if (byteIndex > 0 && location < data.length) {
                data[location] = currentValue;
            }
            
            System.out.println("RAM32: Loaded " + bytesRead + " bytes from " + filepath);
            this.fileLoaded = true;
            return true;
            
        } catch (IOException e) {
            System.err.println("Error loading file: " + e.getMessage());
            return false;
        }
    }

    @Override
    public RamData32 clone() {
        try {
            RamData32 cloned = (RamData32) super.clone();
            cloned.data = this.data.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public int getSize() {
        return this.size;
    }

    public int getDataBits() {
        return this.dataBits;
    }
}

class QuadByteRamName implements StringGetter {
    public String name = "32-bit RAM";

    public QuadByteRamName() {}

    public String toString() {
        return this.name;
    }
}

public class QuadByteRam extends InstanceFactory {
    public static QuadByteRamName componentName = new QuadByteRamName();

    public static final Attribute<Integer> ATTR_SIZE =
        Attributes.forInteger("Number of locations");
    public static final Attribute<BitWidth> ATTR_DATA_BITS =
        Attributes.forBitWidth("Bits per location", 1, 32);

    // Port indices
    private final int DATA_IN = 0;
    private final int DATA_OUT = 1;
    private final int BE = 2;
    private final int CLK = 3;
    private final int WAIT = 4;
    private final int READ = 5;
    private final int WRITE = 6;
    private final int ADDR = 7;

    public QuadByteRam() {
        super("QuadByteRam", new QuadByteRamName());

        setAttributes(
            new Attribute[] { ATTR_SIZE, ATTR_DATA_BITS },
            new Object[] { 1024, BitWidth.create(32) }
        );

        setOffsetBounds(Bounds.create(0, 0, 220, 90));
        updatePorts();
    }

    private void updatePorts() {
        Port[] ports = new Port[8];

        ports[DATA_IN] = new Port(70, 90, Port.INPUT, 32);     // Data Input
        ports[DATA_OUT] = new Port(150, 90, Port.OUTPUT, 32); // Data Output
        ports[BE] = new Port(220, 10, Port.INPUT, 4);           // Byte Enable
        ports[CLK] = new Port(0, 10, Port.INPUT, 1);          // Clock
        ports[WAIT] = new Port(0, 20, Port.OUTPUT, 1);      // Wait
        ports[READ] = new Port(0, 60, Port.INPUT, 1);         // Read
        ports[WRITE] = new Port(0, 70, Port.INPUT, 1);       // Write
        ports[ADDR] = new Port(220, 70, Port.INPUT, 32);       // Address

        setPorts(ports);
    }

    @Override
    protected void configureNewInstance(Instance instance) {
        instance.addAttributeListener();
    }

    @Override
    protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
        if (attr == ATTR_SIZE || attr == ATTR_DATA_BITS) {
            instance.fireInvalidated();
        }
    }

    @Override
    public void propagate(InstanceState state) {
        // Get or create RAM data
        int size = state.getAttributeValue(ATTR_SIZE);
        int dataBits = state.getAttributeValue(ATTR_DATA_BITS).getWidth();
        RamData32 ramData = (RamData32) state.getData();
        if (ramData == null || ramData.getSize() != size || ramData.getDataBits() != dataBits) {
            ramData = new RamData32(size, dataBits);
            state.setData(ramData);
        }

        // Load file if not loaded yet and filename exists
        if (!ramData.isFileLoaded()) {
            // Check if we should load from a file (this would be triggered by menu action)
        }

        // Read input values
        Value clkVal = state.getPortValue(CLK);
        Value addrVal = state.getPortValue(ADDR);
        Value readVal = state.getPortValue(READ);
        Value writeVal = state.getPortValue(WRITE);
        Value beVal = state.getPortValue(BE);
        Value dataInVal = state.getPortValue(DATA_IN);

        // Detect clock edges
        Value lastClk = ramData.getLastClk();
        boolean risingEdge = (lastClk == Value.FALSE && clkVal == Value.TRUE);
        boolean fallingEdge = (lastClk == Value.TRUE && clkVal == Value.FALSE);

        // Check if address is valid
        boolean validAddress = false;
        int addr = 0;
        if (addrVal.isFullyDefined()) {
            addr = (int) addrVal.toLongValue();
            if (addr >= 0 && addr < size) {
                validAddress = true;
            }
        }

        /*
        // Start WAIT signal on falling edge with valid address and (read or write active)
        if (fallingEdge && validAddress && 
            (readVal == Value.TRUE || writeVal == Value.TRUE)) {
            ramData.setWaitCycleCount(2);
        }

        // Decrement wait cycle count on rising edge
        if (risingEdge && ramData.getWaitCycleCount() > 0) {
            ramData.decrementWaitCycle();
        }

        // Set WAIT signal based on cycle count
        boolean waitActive = ramData.getWaitCycleCount() > 0;
        state.setPort(WAIT, waitActive ? Value.TRUE : Value.FALSE, 1);
        */

        // Custom Logic
        int MAX = 3;

        boolean isSelect = fallingEdge && validAddress &&
            (readVal == Value.TRUE || writeVal == Value.TRUE);

        if (isSelect) {
            ramData.incrementWaitCycle();
        }

        if (ramData.getWaitCycleCount() > MAX) {
            ramData.setWaitCycleCount(0);
        }

        boolean shouldWait = ramData.getWaitCycleCount() <= MAX &&
            !(ramData.getWaitCycleCount() == 0);
        state.setPort(
            WAIT,
            shouldWait? Value.TRUE : Value.FALSE,
            1
        );

        // Handle read operation (asynchronous)
        if (readVal == Value.TRUE && validAddress) {
            int memoryValue = ramData.getData(addr);
            Value dataOut = Value.createKnown(BitWidth.create(32), memoryValue);
            state.setPort(DATA_OUT, dataOut, 1);
        } else {
            state.setPort(DATA_OUT, Value.createUnknown(BitWidth.create(32)), 1);
        }

        // Handle write operation (synchronous on rising edge)
        if (risingEdge && writeVal == Value.TRUE && validAddress && dataInVal.isFullyDefined()) {
            int newValue = ramData.getData(addr);
            int inputValue = (int) dataInVal.toLongValue();
            
            // Apply byte enable mask
            if (beVal.isFullyDefined()) {
                int beMask = (int) beVal.toLongValue();
                for (int i = 0; i < 4; i++) {
                    if ((beMask & (1 << i)) != 0) {
                        // Clear the corresponding byte
                        newValue &= ~(0xFF << (i * 8));
                        // Set the new byte value
                        newValue |= (inputValue & (0xFF << (i * 8)));
                    }
                }
            } else {
                // If BE is not defined, write all bytes
                newValue = inputValue;
            }
            
            ramData.setData(addr, newValue);
        }

        ramData.setLastClk(clkVal);
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        painter.drawBounds();
        painter.drawPorts();

        Integer attrSize = painter.getAttributeValue(ATTR_SIZE);
        Integer attrDataBits = painter.getAttributeValue(ATTR_DATA_BITS).getWidth();
        String info = "RAM " +
            attrSize.toString() +
            "x" +
            attrDataBits.toString();

        Bounds bds = painter.getBounds();
        painter.getGraphics().drawString(info, 
            bds.getX() + bds.getWidth() / 2 - 50, 
            bds.getY() + bds.getHeight() / 2);

        painter.getGraphics().drawString(
            "D. IN",
            bds.getX() + 55,
            bds.getY() + 85
        );

        painter.getGraphics().drawString(
            "D. OUT",
            bds.getX() + 134,
            bds.getY() + 85
        );

        painter.getGraphics().drawString(
            "BE",
            bds.getX() + 180,
            bds.getY() + 15
        );

        painter.getGraphics().drawString(
            "CLK",
            bds.getX() + 5,
            bds.getY() + 15
        );

        painter.getGraphics().drawString(
            "WAIT",
            bds.getX() + 5,
            bds.getY() + 26
        );

        painter.getGraphics().drawString(
            "READ",
            bds.getX() + 5,
            bds.getY() + 65
        );

        painter.getGraphics().drawString(
            "WRITE",
            bds.getX() + 5,
            bds.getY() + 76
        );

        painter.getGraphics().drawString(
            "ADDR",
            bds.getX() + 180,
            bds.getY() + 75
        );
    }

    @Override
    protected Object getInstanceFeature(Instance instance, Object key) {
        return (key == MenuExtender.class)
            ? new QuadByteRamMenu(instance)
            : super.getInstanceFeature(instance, key);
    }
}