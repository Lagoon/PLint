package models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product extends MyModel {

	@Column(name = "name")
	public String name;

	@Column(name = "price")
	public int  price;

	@ManyToOne
	public Type type;

	@ManyToOne
	public Condition condition;

	@ManyToOne
	public User owner;

	@OneToOne
	public Purchase purchase;

	@OneToMany(mappedBy="product", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch=FetchType.EAGER)
	public Set<Negociation> negociations = new HashSet<Negociation>(0);
	
	///	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String toString() {
		return name;
	}
}