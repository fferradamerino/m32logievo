package m32plugin;

import com.cburch.logisim.util.StringGetter;

class ComponentName implements StringGetter{
	String name = "Defined Output Splitter";
	
	public ComponentName() {}

	public String toString() {
		return name;
	}
}
