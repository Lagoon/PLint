package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "negociation")
public class Negociation extends MyModel {

	@ManyToOne
	public Product product;

	@ManyToOne
	public User author;
}