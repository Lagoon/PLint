package controllers;

import java.util.List;

import models.Negociation;
import play.data.validation.Valid;
import play.i18n.Messages;

public class Negociations extends Application {
	public static void index() {
		List<Negociation> entities = models.Negociation.all().fetch();
		render(entities);
	}

	public static void create(Negociation entity) {
		render(entity);
	}

	public static void show(java.lang.Long id) {
		Negociation entity = Negociation.findById(id);
		render(entity);
	}

	public static void edit(java.lang.Long id) {
		Negociation entity = Negociation.findById(id);
		render(entity);
	}

	public static void delete(java.lang.Long id) {
		Negociation entity = Negociation.findById(id);
		entity.delete();
		index();
	}

	public static void save(@Valid Negociation entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@create", entity);
		}
		entity.save();
		flash.success(Messages.get("scaffold.created", "Negociation"));
		index();
	}

	public static void update(@Valid Negociation entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}

		entity = entity.merge();

		entity.save();
		flash.success(Messages.get("scaffold.updated", "Negociation"));
		index();
	}
}
