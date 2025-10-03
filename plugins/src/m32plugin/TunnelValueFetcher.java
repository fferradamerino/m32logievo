import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.std.wiring.Tunnel;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.StdAttr;

public class TunnelValueFetcher {
    
    public Value getTunnelValue(CircuitState circuitState, String tunnelLabel) {
        Circuit circuit = circuitState.getCircuit();

        for (Component comp : circuit.getNonWires()) {
            if (comp.getFactory() instanceof Tunnel) {
                String label = comp.getAttributeSet()
                    .getValue(StdAttr.LABEL).toString();
                
                if (label.equals(tunnelLabel)) {
                    return circuitState.getValue(comp.getLocation());
                }
            }
        }
        
        return Value.UNKNOWN;
    }
}