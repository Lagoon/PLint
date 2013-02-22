package ls;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.google.gson.annotations.Expose;

public class LSPermission implements Serializable {

	@Expose
	public String profile;

	@Expose
	public HashMap<String, List<String>> actions;

	//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public String getProfileName() {
		return profile;
	}

	public void setProfileName(String profileName) {
		this.profile = profileName;
	}
}