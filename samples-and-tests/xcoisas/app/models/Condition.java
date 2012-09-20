package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "status")
public class Condition extends MyModel {

	@Column(name = "name")
	public String name;

	@OneToMany(cascade={CascadeType.REMOVE}, mappedBy="type")
	public Set<Product> products = new HashSet<Product>(0);

	///	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Condition(String name)
	{
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public static Condition findByName(String name) {
		return find("byName", name).first();
	}
}
