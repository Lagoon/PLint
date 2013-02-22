package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User extends MyModel {

	public Long externalID;

	@OneToMany(mappedBy="owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	public Set<Product> products = new HashSet<Product>(0);

	@OneToMany(mappedBy="buyer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	public Set<Purchase> purchases = new HashSet<Purchase>(0);

	@OneToMany(mappedBy="author", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	public Set<Negociation> negociations = new HashSet<Negociation>(0);

	///	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static User findByExternalID(long externalID) {
		return find("byexternalID", externalID).first();
	}

	public String toString() {
		return String.valueOf(externalID);
	}
}