package models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

public class Permission implements Serializable {

	@Expose
	public String profile;

	@Expose
	public Map<String, List<String>> actions;

	public String getProfileName() {
		return profile;
	}
	public void setProfileName(String profileName) {
		this.profile = profileName;
	}
	public Map<String, List<String>> getAction_aps() {
		return actions;
	}
	public void setAction_aps(Map<String, List<String>> action_aps) {
		this.actions = action_aps;
	}
}