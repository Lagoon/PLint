package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User extends MyModel {

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	public UserLagoon userLagoon;

	@ManyToOne
	public Context context;

	@ManyToMany
	public Set<Profile> profiles = new HashSet<Profile>(0);


	@OneToMany(mappedBy="owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	public Set<Product> products = new HashSet<Product>(0);

	@OneToMany(mappedBy="buyer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	public Set<Purchase> purchases = new HashSet<Purchase>(0);

	@OneToMany(mappedBy="author", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	public Set<Negociation> negociations = new HashSet<Negociation>(0);

	///	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public User(UserLagoon userLagoon, Context context ) {
		this.userLagoon = userLagoon;
		this.context = context;
	}

	public static User findByUserLagoon(UserLagoon userLagoon) {
		return find("byUserLagoon", userLagoon).first();
	}
	
	public String toString() {
		return userLagoon.login;
	}
}