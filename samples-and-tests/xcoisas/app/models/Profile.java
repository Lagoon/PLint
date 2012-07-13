package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.jpa.GenericModel;

@Entity
public class Profile extends GenericModel {

	@Id
	@GeneratedValue
	public Long profileID;

	@Column(name="id")
	public Long id;

	@Column(name="name")
	public String name;

	@ManyToMany(mappedBy = "profiles")
	Set<User> users = new HashSet<User>(0);

	///	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public String toString() {
		return name;
	}

	public static Profile findByExternalId(Long id) {
		return find("byId", id).first();
	}

}
