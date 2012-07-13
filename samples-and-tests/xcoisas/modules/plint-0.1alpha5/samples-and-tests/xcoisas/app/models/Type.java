package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "type")
public class Type extends MyModel {

	@Column(name = "name")
	public String name;

	@OneToMany(cascade={CascadeType.REMOVE}, mappedBy="type")
	public Set<Product> products = new HashSet<Product>(0);

	///	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public String toString() {
		return name;
	}
}