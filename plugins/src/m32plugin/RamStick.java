package m32plugin;

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
import com.cburch.logisim.util.StringGetter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class StickData32 implements InstanceData, Cloneable {
    private int[] data;
    private String filename;
    private Value lastClk;
    private boolean fileLoaded;
    private int size;
    private int dataBits;

    public StickData32(int size, int dataBits) {
        this.size = size;
        this.dataBits = dataBits;
        this.data = new int[size];
        // Initialize with zeros
        for (int i = 0; i < size; i++) {
            data[i] = 0;
        }
        lastClk = Value.FALSE;
        fileLoaded = false;
    }

    public int getData(int addr) {
        addr = addr / 4;
        if (addr >= 0 && addr < data.length) {
            return this.data[addr];
        }
        return 0;
    }

    public void setData(int addr, int val) {
        addr = addr / 4;
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
                currentValue |= (value & 0xFF) << ((bytesPerLocation - 1 - byteIndex) * 8);
                byteIndex++;
                bytesRead++;
                
                if (byteIndex >= bytesPerLocation) {
                    data[location] = currentValue;
                    location++;
                    currentValue = 0;
                    byteIndex = 0;
                }
            }
            
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
    public StickData32 clone() {
        try {
            StickData32 cloned = (StickData32) super.clone();
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

class RamStickName implements StringGetter {
    public String name = "Memory Stick";

    public RamStickName() {}

    public String toString() {
        return this.name;
    }
}

public class RamStick extends InstanceFactory {
    public static RamStickName componentName = new RamStickName();

    public static final Attribute<Integer> ATTR_SIZE =
        Attributes.forInteger("Number of locations");
    public static final Attribute<BitWidth> ATTR_DATA_BITS =
        Attributes.forBitWidth("Bits per location", 1, 32);

    // Port indices
    private final int DATA_IN = 0;
    private final int DATA_OUT = 1;
    private final int CLK = 2;
    private final int READ = 3;
    private final int WRITE = 4;
    private final int ADDR = 5;
    private final int CS = 6;

    public RamStick() {
        super("RamStick", new RamStickName());

        setAttributes(
            new Attribute[] { ATTR_SIZE, ATTR_DATA_BITS },
            new Object[] { 1024, BitWidth.create(32) }
        );

        setOffsetBounds(Bounds.create(0, 0, 90, 200));
        updatePorts();
    }

    private void updatePorts() {
        Port[] ports = new Port[7];

        ports[DATA_IN] = new Port(50, 200, Port.INPUT, 32);
        ports[DATA_OUT] = new Port(70, 200, Port.OUTPUT, 32);
        ports[CLK] = new Port(80, 0, Port.INPUT, 1);
        ports[READ] = new Port(20, 0, Port.INPUT, 1);
        ports[WRITE] = new Port(40, 0, Port.INPUT, 1);
        ports[ADDR] = new Port(60, 0, Port.INPUT, 32);
        ports[CS] = new Port(20, 200, Port.INPUT, 1);

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
        StickData32 ramData = (StickData32) state.getData();
        if (ramData == null || ramData.getSize() != size || ramData.getDataBits() != dataBits) {
            ramData = new StickData32(size, dataBits);
            
            // Programa inicial (ver diapositivas de clase)
            ramData.setData(0, 19439632);
            ramData.setData(4, 27303956);
            ramData.setData(8, 152654848);
            ramData.setData(12, 102277144);
            ramData.setData(16, 25);
            ramData.setData(20, 5);
            ramData.setData(24, 0);

            state.setData(ramData);
        }

        // Read input values
        Value clkVal = state.getPortValue(CLK);
        Value addrVal = state.getPortValue(ADDR);
        Value readVal = state.getPortValue(READ);
        Value writeVal = state.getPortValue(WRITE);
        Value dataInVal = state.getPortValue(DATA_IN);
        Value csVal = state.getPortValue(CS);

        // Check if chip is selected (active low: FALSE = selected)
        boolean chipSelected = (csVal == Value.FALSE);

        // If chip is not selected, output high-impedance and return early
        if (!chipSelected) {
            state.setPort(DATA_OUT, Value.createUnknown(BitWidth.create(32)), 1);
            ramData.setLastClk(clkVal);
            return;
        }

        // Detect clock edges
        Value lastClk = ramData.getLastClk();
        boolean risingEdge = (lastClk == Value.FALSE && clkVal == Value.TRUE);

        // Check if address is valid
        boolean validAddress = false;
        int addr = 0;
        if (addrVal.isFullyDefined()) {
            addr = (int) addrVal.toLongValue();
            if (addr >= 0 && addr < size) {
                validAddress = true;
            }
        }

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
            int inputValue = (int) dataInVal.toLongValue();
            ramData.setData(addr, inputValue);
        }

        ramData.setLastClk(clkVal);
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        painter.drawBounds();
        painter.drawPorts();

        Integer attrSize = painter.getAttributeValue(ATTR_SIZE);
        Integer attrDataBits = painter.getAttributeValue(ATTR_DATA_BITS).getWidth();

        String sizeLabel;
        if (attrSize > 1000) {
            Integer temp = attrSize / 1024;
            sizeLabel = temp.toString() + "K";
        } else {
            sizeLabel = attrSize.toString();
        }

        String info =
            sizeLabel +
            "x" +
            attrDataBits.toString();

        Bounds bds = painter.getBounds();
        painter.getGraphics().drawString(info, 
            bds.getX() + bds.getWidth() / 2 - 20, 
            bds.getY() + bds.getHeight() / 2);

        painter.getGraphics().drawString(
            "IN",
            bds.getX() + 45,
            bds.getY() + 190
        );

        painter.getGraphics().drawString(
            "O",
            bds.getX() + 65,
            bds.getY() + 190
        );

        painter.getGraphics().drawString(
            "CK",
            bds.getX() + 70,
            bds.getY() + 15
        );

        painter.getGraphics().drawString(
            "CS",
            bds.getX() + 15,
            bds.getY() + 190
        );

        painter.getGraphics().drawString(
            "R",
            bds.getX() + 15,
            bds.getY() + 15
        );

        painter.getGraphics().drawString(
            "W",
            bds.getX() + 35,
            bds.getY() + 15
        );

        painter.getGraphics().drawString(
            "A",
            bds.getX() + 55,
            bds.getY() + 15
        );
    }
}