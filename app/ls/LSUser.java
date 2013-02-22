package ls;

import java.io.Serializable;
import java.util.ArrayList;

public class LSUser implements Serializable{

	public Long id;

	public Boolean enable;

	public Boolean ghost;

	public String login;

	public String name;

	public String email;

	public String token;

	public ArrayList<LSProfile> profiles = new ArrayList<LSProfile>();


	//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public LSUser() {}

	@Override
	public String toString() {
		return name;
	}
}
