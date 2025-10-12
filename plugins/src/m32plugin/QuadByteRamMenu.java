import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.cburch.logisim.circuit.CircuitState;
import com.cburch.logisim.instance.Instance;
import com.cburch.logisim.proj.Project;
import com.cburch.logisim.tools.MenuExtender;

public class QuadByteRamMenu implements ActionListener, MenuExtender {
    public Project proj;
    public Frame frame;

    public Instance instance;

    private CircuitState circState;
    private JMenuItem load;

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

        menu.addSeparator();
        menu.add(load);
    }

    private void doLoad() {
        // Do the actual file load
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object src = evt.getSource();
        if (src == load) doLoad();
    }
}