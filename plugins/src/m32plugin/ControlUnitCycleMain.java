package m32plugin;

import com.cburch.logisim.data.*;
import com.cburch.logisim.instance.*;
import com.cburch.logisim.util.GraphicsUtil;
import com.cburch.logisim.circuit.Circuit;
import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.comp.Component;
import com.cburch.logisim.std.wiring.Tunnel;
import com.cburch.logisim.util.StringGetter;

import java.awt.*;

class NombreDelTunnel implements StringGetter {
    public String toString() {
        return "Nombre del Tunnel";
    }
}

class ControlUnitCycleMainName implements StringGetter {
    public String toString() {
        return "Control Unit Cycle (main)";
    }
}

public class ControlUnitCycleMain extends InstanceFactory {
    public static ControlUnitCycleMainName componentName = new ControlUnitCycleMainName();
    private static NombreDelTunnel nombreDelTunnel = new NombreDelTunnel();

    public static final Attribute<String> ATTR_TUNNEL_NAME = 
        Attributes.forString("tunnelName", nombreDelTunnel);
    
    public ControlUnitCycleMain() {
        super("ControlUnitCycleMain");
        setAttributes(
            new Attribute[] { ATTR_TUNNEL_NAME },
            new Object[] { "ADDR" }
        );
        setOffsetBounds(Bounds.create(-30, -20, 60, 40));
    }
    
    @Override
    protected void configureNewInstance(Instance instance) {
        instance.addAttributeListener();
    }
    
    @Override
    public void propagate(InstanceState state) {
        // No propagamos señales, solo mostramos información
    }
    
    @Override
    public void paintInstance(InstancePainter painter) {
        Graphics g = painter.getGraphics();
        Bounds bds = painter.getBounds();
        
        // Dibujar fondo y borde
        g.setColor(Color.WHITE);
        g.fillRect(bds.getX(), bds.getY(), bds.getWidth(), bds.getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(bds.getX(), bds.getY(), bds.getWidth(), bds.getHeight());
        
        // Obtener el estado del CPU
        String stateText = getCPUState(painter);
        
        // Configurar y dibujar texto
        Font font = g.getFont().deriveFont(Font.BOLD, 10f);
        g.setFont(font);
        g.setColor(Color.BLUE);
        
        GraphicsUtil.drawCenteredText(g, stateText, 
            bds.getX() + bds.getWidth() / 2, 
            bds.getY() + bds.getHeight() / 2);
        
        painter.drawLabel();
    }
    
    /**
     * Obtiene el estado del CPU basado en el valor del tunnel ADDR
     */
    private String getCPUState(InstancePainter painter) {
        try {
            // Obtener el CircuitState desde el painter
            CircuitState circuitState = painter.getCircuitState();
            if (circuitState == null) return "NO CIRCUIT";

            // Obtener configuración
            InstanceState state = circuitState.getInstanceState(painter.getInstance());
            String tunnelName = state.getAttributeValue(ATTR_TUNNEL_NAME);
            
            // Navegar hacia el circuito raíz (main)
            CircuitState mainState = circuitState;
            while (mainState.getParentState() != null) {
                mainState = mainState.getParentState();
            }
           
            // Buscar subcircuito M32
            CircuitState m32State = findSubcircuit(mainState, "M32");
            if (m32State == null) return "NO M32";
            
            // Buscar subcircuito ControlUnit dentro de M32
            CircuitState controlUnitState = findSubcircuit(m32State, "ControlUnit");
            if (controlUnitState == null) return "NO CU";
            
            // Buscar el valor del tunnel en ControlUnit
            Value addrValue = findTunnelValue(controlUnitState, tunnelName);
            if (addrValue == null || !addrValue.isFullyDefined()) {
                return "UNDEFINED";
            }
            
            // Convertir el valor a entero
            int addr = Integer.parseInt(addrValue.toDecimalString(false));
            
            // Retornar el estado correspondiente
            return getStateFromAddr(addr);
            
        } catch (Exception e) {
            return "ERROR";
        }
    }
    
    /**
     * Busca un subcircuito por nombre dentro de un CircuitState padre
     */
    private CircuitState findSubcircuit(CircuitState parent, String name) {
        Circuit parentCircuit = parent.getCircuit();
        
        // Primero buscar el componente en el circuito padre
        for (Component comp : parentCircuit.getNonWires()) {
            if (comp.getFactory() instanceof com.cburch.logisim.circuit.SubcircuitFactory) {
                com.cburch.logisim.circuit.SubcircuitFactory factory = 
                    (com.cburch.logisim.circuit.SubcircuitFactory) comp.getFactory();
                
                String circuitName = factory.getSubcircuit().getName();
                if (circuitName.equals(name)) {
                    // Ahora buscar el estado correspondiente en los substates
                    for (CircuitState subState : parent.getSubStates()) {
                        if (subState.getCircuit().getName().equals(name)) {
                            return subState;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Busca un tunnel por nombre y retorna su valor
     */
    private Value findTunnelValue(CircuitState state, String tunnelName) {
        Circuit circuit = state.getCircuit();
        
        for (Component comp : circuit.getNonWires()) {
            if (comp.getFactory() instanceof Tunnel) {
                String label = comp.getAttributeSet().getValue(StdAttr.LABEL);
                if (label != null && label.equals(tunnelName)) {
                    // Obtener el valor en la ubicación del pin del tunnel
                    return state.getValue(comp.getEnd(0).getLocation());
                }
            }
        }
        return null;
    }
    
    /**
     * Determina el estado del CPU según el valor de ADDR
     */
    private String getStateFromAddr(int addr) {
        if (addr == 0) {
            return "FETCH 1";
        } else if (addr == 1 || addr == 2) {
            return "FETCH 2";
        } else if (addr == 3 || addr == 4) {
            return "DECOD";
        } else {
            return "EXECUTE";
        }
    }
}