package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "purchase")
public class Purchase extends MyModel {

	@ManyToOne
	public User buyer;

	@OneToOne
	public Product product;

	@Column(name = "price")
	public int  price;
}