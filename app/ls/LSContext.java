package ls;

import java.io.Serializable;

public class LSContext implements Serializable{

	public Long id;
	public Boolean enable;
	public Boolean defaultContext;
	public String name;
	public String description;
	public String activationUrl;
	public String url;

	//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public LSContext() {}

	@Override
	public String toString() {
		return name;
	}
}
