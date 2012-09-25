package controllers;

import java.util.List;

import models.UserLagoon;
import play.data.validation.Valid;
import play.db.jpa.JPA;
import play.i18n.Messages;

public class UserLagoons extends Application {
	public static void index() {
		List<UserLagoon> entities = play.db.jpa.JPA.em().createQuery("from UserLagoon").getResultList();
		render(entities);
	}

	public static void create(UserLagoon entity) {
		render(entity);
	}

	public static void show(java.lang.Long id) {
		UserLagoon entity = JPA.em().find(models.UserLagoon.class, id);
		render(entity);
	}

	public static void edit(java.lang.Long id) {
		UserLagoon entity = JPA.em().find(models.UserLagoon.class, id);
		render(entity);
	}

	public static void delete(java.lang.Long id) {
		UserLagoon entity = JPA.em().find(models.UserLagoon.class, id);
		JPA.em().remove(entity);
		index();
	}

	public static void save(@Valid UserLagoon entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@create", entity);
		}
		JPA.em().persist(entity);
		flash.success(Messages.get("scaffold.created", "UserLagoon"));
		index();
	}

	public static void update(@Valid UserLagoon entity) {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}

		JPA.em().merge(entity);
		flash.success(Messages.get("scaffold.updated", "UserLagoon"));
		index();
	}
}
