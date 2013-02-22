package ls;

import java.io.Serializable;

public class LSApp implements Serializable{

	public String description;
	public String name;

	//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public LSApp() {}

	@Override
	public String toString() {
		return name;
	}
}
