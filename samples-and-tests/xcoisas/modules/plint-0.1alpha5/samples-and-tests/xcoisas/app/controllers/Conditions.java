package controllers;

import java.util.List;

import models.Condition;
import play.data.validation.Valid;
import play.i18n.Messages;

public class Conditions extends Application {
	public static void index() {
		List<Condition> entities = models.Condition.all().fetch();
		render(entities);
	}

	public static void create(Condition entity) {
		render(entity);
	}

	public static void show(java.lang.Long id) {
		Condition entity = Condition.findById(id);
		render(entity);
	}

	public static void edit(java.lang.Long id) {
		Condition entity = Condition.findById(id);
		render(entity);
	}

	public static void delete(java.lang.Long id) {
		Condition entity = Condition.findById(id);
		entity.delete();
		index();
	}

	public static void save(@Valid Condition entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@create", entity);
		}
		entity.save();
		flash.success(Messages.get("scaffold.created", "Condition"));
		index();
	}

	public static void update(@Valid Condition entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}

		entity = entity.merge();

		entity.save();
		flash.success(Messages.get("scaffold.updated", "Condition"));
		index();
	}
}
