package controllers;

import java.util.List;

import models.Condition;
import models.Product;
import models.Purchase;
import models.User;
import models.UserLagoon;
import play.data.validation.Valid;
import play.i18n.Messages;

public class Products extends Application {
	public static void index() {
		List<Product> entities = models.Product.all().fetch();
		render(entities);
	}

	public static void create(Product entity) {
		render(entity);
	}

	public static void show(java.lang.Long id) {
		Product entity = Product.findById(id);
		render(entity);
	}

	public static void edit(java.lang.Long id) {
		Product entity = Product.findById(id);
		render(entity);
	}
	
	public static void buy(java.lang.Long id) {
		Product p = Product.findById(id);
		Purchase purchase = new Purchase();
		
		UserLagoon userLagoon = UserLagoon.findByExternalID(Long.parseLong(session.get("id")));
		User buyer = User.findByUserLagoon(userLagoon);
		
		purchase.product = p;
		purchase.price = p.price;
		purchase.buyer = buyer;
		purchase.save();
		
		//Mark as sold 
		Condition sold = Condition.findByName("sold");
		if(sold == null) {
			sold = new Condition("sold");
			sold.save();
		}
		p.condition = sold;
		doFlashSuccess("Product purchased");
		index();
	}
	

	public static void delete(java.lang.Long id) {
		Product entity = Product.findById(id);
		entity.delete();
		index();
	}

	public static void save(@Valid Product entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@create", entity);
		}
		
		UserLagoon userLagoon = UserLagoon.findByExternalID(Long.parseLong(session.get("id")));
		User owner = User.findByUserLagoon(userLagoon);
		
		entity.owner = owner;
		entity.save();
		flash.success(Messages.get("scaffold.created", "Product"));
		index();
	}

	public static void update(@Valid Product entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}

		entity = entity.merge();

		entity.save();
		flash.success(Messages.get("scaffold.updated", "Product"));
		index();
	}
}
