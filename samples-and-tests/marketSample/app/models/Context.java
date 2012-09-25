package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.GenericModel;

@Entity
@Table(name = "context")
public class Context extends GenericModel {

	@Id
	@GeneratedValue
	public Long contextID;

	@Required
	@Column(name = "name")
	public String name;

	@Column(name = "description")
	public String description;

	@OneToMany(cascade={CascadeType.REMOVE}, mappedBy="context")
	public Set<User> users = new HashSet<User>(0);

	///	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static Context findByName(String name) {
		return find("byName", name).first();
	}
	
	public String toString() {
		return name;
	}
}