package ls;

import java.io.Serializable;

public class LSInstance implements Serializable{

	public Boolean notify;
	public String activationUrl;
	public String description;
	public String name;
	public String url;
	public LSApp application;

	//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public LSInstance() {}

	@Override
	public String toString() {
		return name;
	}
}
