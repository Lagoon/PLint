package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.jpa.GenericModel;

@Entity
public class UserLagoon extends GenericModel {

	@Id
	@GeneratedValue
	public Long userLagoonId;

	public Long id;

	public String login;

	public String name;

	public String email;

	public int profileId;

	@ManyToMany
	public Set<UserPermission> userAccessPermission = new HashSet<UserPermission>(0);


	//	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public UserLagoon() {
	}

	@Override
	public String toString() {
		return name;
	}


	public static UserLagoon findByExternalID(Long id) {
		return find("byId", id).first();
	}
}
