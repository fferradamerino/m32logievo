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

class RamData implements InstanceData, Cloneable {
    private Byte data[];
    private String filename;
    private Value lastClk;

    public RamData(int bits) {
        data = new Byte[(2 << (bits - 1)) * 4];
        // Inicializar con ceros
        for (int i = 0; i < data.length; i++) {
            data[i] = 0;
        }
        lastClk = Value.FALSE;
    }

    public Byte getData(int addr) {
        if (addr >= 0 && addr < data.length) {
            return this.data[addr];
        }
        return 0;
    }

    public void setData(int addr, byte val) {
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

    @Override
    public RamData clone() {
        try {
            RamData cloned = (RamData) super.clone();
            cloned.data = this.data.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

class QuadByteRamName implements StringGetter {
    public String name = "QuadByteRam";

    public QuadByteRamName() {}

    public String toString() {
        return this.name;
    }
}

public class QuadByteRam extends InstanceFactory {
    public static QuadByteRamName componentName = new QuadByteRamName();

    public static final Attribute<String> ATTR_FILENAME =
        Attributes.forString("Nombre de archivo");
    public static final Attribute<BitWidth> ATTR_BITWIDHT =
        Attributes.forBitWidth("Ancho de bits", 1, 21);

    private int IN_0 = 0;
    private int IN_1 = 1;
    private int IN_2 = 2;
    private int IN_3 = 3;

    private int ADDR = 4;
    private int CLK = 5;
    private int WAIT = 6;

    private int OUT_0 = 7;
    private int OUT_1 = 8;
    private int OUT_2 = 9;
    private int OUT_3 = 10;

    public QuadByteRam() {
        super("QuadByteRam", new QuadByteRamName());

        Port[] ports = new Port[11];

        Bounds bounds = Bounds.create(0, 0, 100, 100);

        setOffsetBounds(bounds);

        ports[IN_0] = new Port(0, 10, Port.INPUT, 8); // Input0
        ports[IN_1] = new Port(0, 20, Port.INPUT, 8); // Input1
        ports[IN_2] = new Port(0, 30, Port.INPUT, 8); // Input2
        ports[IN_3] = new Port(0, 40, Port.INPUT, 8); // Input3

        ports[ADDR] = new Port(0, 80, Port.INPUT, 21); // ADDR
        ports[CLK] = new Port(0, 90, Port.INPUT, 1); // CLK
        ports[WAIT] = new Port(100, 90, Port.OUTPUT, 1); // WAIT

        ports[OUT_0] = new Port(100, 10, Port.OUTPUT, 8); // Output0
        ports[OUT_1] = new Port(100, 20, Port.OUTPUT, 8); // Output1
        ports[OUT_2] = new Port(100, 30, Port.OUTPUT, 8); // Output2
        ports[OUT_3] = new Port(100, 40, Port.OUTPUT, 8); // Output3

        setPorts(ports);

        setAttributes(
            new Attribute[] { ATTR_FILENAME, ATTR_BITWIDHT },
            new Object[] { "", BitWidth.create(21) }
        );
    }

    @Override
    protected void configureNewInstance(Instance instance) {
        instance.addAttributeListener();
    }

    @Override
    protected void instanceAttributeChanged(Instance instance, Attribute<?> attr) {
        if (attr == ATTR_BITWIDHT) {
            updatePorts(instance);
            // Forzar la reinicialización de los datos de RAM con el nuevo tamaño
            instance.fireInvalidated();
        }
    }

    @Override
    public void propagate(InstanceState state) {
        // Obtener o crear datos de la RAM
        RamData ramData = (RamData) state.getData();
        if (ramData == null) {
            BitWidth bitWidth = state.getAttributeValue(ATTR_BITWIDHT);
            ramData = new RamData(bitWidth.getWidth());
            state.setData(ramData);
        }

        // Leer valores de entrada
        Value clkVal = state.getPortValue(CLK);
        Value addrVal = state.getPortValue(ADDR);
        
        // Detectar flanco de subida del reloj
        boolean risingEdge = ramData.getLastClk() == Value.FALSE && clkVal == Value.TRUE;
        ramData.setLastClk(clkVal);

        // Validar dirección
        if (!addrVal.isFullyDefined()) {
            // Si la dirección no está definida, salidas en estado desconocido
            state.setPort(OUT_0, Value.createUnknown(BitWidth.create(8)), 1);
            state.setPort(OUT_1, Value.createUnknown(BitWidth.create(8)), 1);
            state.setPort(OUT_2, Value.createUnknown(BitWidth.create(8)), 1);
            state.setPort(OUT_3, Value.createUnknown(BitWidth.create(8)), 1);
            state.setPort(WAIT, Value.FALSE, 1);
            return;
        }

        int addr = Integer.parseInt(addrVal.toDecimalString(false)) * 4; // Multiplicar por 4 porque cada dirección tiene 4 bytes

        // En flanco de subida, escribir datos
        if (risingEdge) {
            Value in0 = state.getPortValue(IN_0);
            Value in1 = state.getPortValue(IN_1);
            Value in2 = state.getPortValue(IN_2);
            Value in3 = state.getPortValue(IN_3);

            if (in0.isFullyDefined()) {
                ramData.setData(addr, (byte) Integer.parseInt(in0.toDecimalString(false)));
            }
            if (in1.isFullyDefined()) {
                ramData.setData(addr + 1, (byte) Integer.parseInt(in1.toDecimalString(false)));
            }
            if (in2.isFullyDefined()) {
                ramData.setData(addr + 2, (byte) Integer.parseInt(in2.toDecimalString(false)));
            }
            if (in3.isFullyDefined()) {
                ramData.setData(addr + 3, (byte) Integer.parseInt(in3.toDecimalString(false)));
            }
        }

        // Leer datos (siempre, de forma asíncrona)
        Byte byte0 = ramData.getData(addr);
        Byte byte1 = ramData.getData(addr + 1);
        Byte byte2 = ramData.getData(addr + 2);
        Byte byte3 = ramData.getData(addr + 3);

        // Convertir bytes a valores de Logisim (manejar valores sin signo)
        Value out0 = Value.createKnown(BitWidth.create(8), byte0 & 0xFF);
        Value out1 = Value.createKnown(BitWidth.create(8), byte1 & 0xFF);
        Value out2 = Value.createKnown(BitWidth.create(8), byte2 & 0xFF);
        Value out3 = Value.createKnown(BitWidth.create(8), byte3 & 0xFF);

        // Establecer salidas
        state.setPort(OUT_0, out0, 1);
        state.setPort(OUT_1, out1, 1);
        state.setPort(OUT_2, out2, 1);
        state.setPort(OUT_3, out3, 1);
        
        // WAIT siempre en FALSE (sin estado de espera)
        state.setPort(WAIT, Value.FALSE, 1);
    }

    void updatePorts(Instance instance) {
        BitWidth bitWidth = instance.getAttributeValue(ATTR_BITWIDHT);
        
        Port[] ports = new Port[11];

        ports[IN_0] = new Port(0, 10, Port.INPUT, 8); // Input0
        ports[IN_1] = new Port(0, 20, Port.INPUT, 8); // Input1
        ports[IN_2] = new Port(0, 30, Port.INPUT, 8); // Input2
        ports[IN_3] = new Port(0, 40, Port.INPUT, 8); // Input3

        // Actualizar el ancho del puerto ADDR según el atributo
        ports[ADDR] = new Port(0, 80, Port.INPUT, bitWidth.getWidth()); // ADDR con ancho variable
        ports[CLK] = new Port(0, 90, Port.INPUT, 1); // CLK
        ports[WAIT] = new Port(100, 90, Port.OUTPUT, 1); // WAIT

        ports[OUT_0] = new Port(100, 10, Port.OUTPUT, 8); // Output0
        ports[OUT_1] = new Port(100, 20, Port.OUTPUT, 8); // Output1
        ports[OUT_2] = new Port(100, 30, Port.OUTPUT, 8); // Output2
        ports[OUT_3] = new Port(100, 40, Port.OUTPUT, 8); // Output3

        instance.setPorts(ports);
    }

    @Override
    public void paintInstance(InstancePainter instancePainter) {
        instancePainter.drawBounds();
        instancePainter.drawPorts();
    }
}