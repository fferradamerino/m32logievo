package m32plugin;

import com.cburch.logisim.tools.FactoryDescription;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;
import java.util.List;

public class M32Library extends Library {
    private List<Tool> tools = null;

    public static final String _ID = "Soc";

    private static final FactoryDescription[] DESCRIPTIONS = {
        new FactoryDescription(DefinedOutputSplitter32.class, DefinedOutputSplitter32.componentName)
    };

    public M32Library() {}

    @Override
    public String getDisplayName() {
        return "Biblioteca de componentes M32";
    }

    public List<Tool> getTools() {
        if (tools == null) {
            tools = FactoryDescription.getTools(M32Library.class, DESCRIPTIONS);
        }
        return tools;
    }
}
