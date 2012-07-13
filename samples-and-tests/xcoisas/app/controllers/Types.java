package controllers;

import java.util.List;

import models.Type;
import play.data.validation.Valid;
import play.i18n.Messages;

public class Types extends Application {
	public static void index() {
		List<Type> entities = models.Type.all().fetch();
		render(entities);
	}

	public static void create(Type entity) {
		render(entity);
	}

	public static void show(java.lang.Long id) {
		Type entity = Type.findById(id);
		render(entity);
	}

	public static void edit(java.lang.Long id) {
		Type entity = Type.findById(id);
		render(entity);
	}

	public static void delete(java.lang.Long id) {
		Type entity = Type.findById(id);
		entity.delete();
		index();
	}

	public static void save(@Valid Type entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@create", entity);
		}
		entity.save();
		flash.success(Messages.get("scaffold.created", "Type"));
		index();
	}

	public static void update(@Valid Type entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}

		entity = entity.merge();

		entity.save();
		flash.success(Messages.get("scaffold.updated", "Type"));
		index();
	}
}
