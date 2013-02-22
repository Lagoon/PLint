package ls;

import java.io.Serializable;

public class LSProfile implements Serializable{

	public Long id;

	public String description;

	public String name;

	//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public LSProfile() {}

	@Override
	public String toString() {
		return name;
	}
}
