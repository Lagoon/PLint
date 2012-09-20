package controllers;

import java.util.List;
import java.util.concurrent.TimeoutException;

import lagoon.LintRobot;
import models.Context;
import play.data.validation.Valid;
import play.i18n.Messages;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import exceptions.LintException;

public class Contexts extends Application {
	public static void index() {
		List<Context> entities = models.Context.all().fetch();
		render(entities);
	}

	public static void create(Context entity) {
		render(entity);
	}

	public static void show(java.lang.Long id) throws LintException, TimeoutException {
		Context ctx = Context.findById(id);
		JsonElement resp = LintRobot.showContext(ctx.name);
		Gson gson = new Gson();
		Context entity = gson.fromJson(resp, Context.class);
		render(entity);
	}

	public static void edit(java.lang.Long id) {
		Context entity = Context.findById(id);
		String old = entity.name;
		render(entity, old);
	}

	public static void delete(java.lang.Long id) throws LintException, TimeoutException {
		Context entity = Context.findById(id);

		LintRobot.deleteContext(entity.name);
		entity.delete();
		index();
	}

	public static void save(@Valid Context entity) throws LintException, TimeoutException {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@create", entity);
		}
		// Send create request
		JsonElement resp = LintRobot.createContext(entity.name, null, null, entity.description, null);
		Gson gson = new Gson();
		Context context = gson.fromJson(resp, Context.class);
		context.save();
		doFlashSuccess("Context created");
		index();
	}

	public static void update(@Valid Context entity, String old) throws LintException, TimeoutException {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}

		// Send Update request
		LintRobot.updateContext(entity.name, null, null, entity.description, old);
		entity.merge();
		entity.save();

		doFlashSuccess("Context updated");
		index();
	}
}
