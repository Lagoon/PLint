package controllers;

import java.util.List;
import java.util.concurrent.TimeoutException;

import lagoon.PlintRobot;
import ls.LSContext;
import play.data.validation.Valid;
import play.i18n.Messages;
import exceptions.LintException;

public class Contexts extends Application {

	public static void index() throws LintException, TimeoutException {
		List<LSContext> entities = PlintRobot.getInstance().getContexts();
		render(entities);
	}

	public static void create(LSContext entity) {
		render(entity);
	}

	public static void show(String name) throws LintException, TimeoutException {
		LSContext entity = PlintRobot.getInstance().getContext(name);
		render(entity);
	}

	public static void edit(String name) throws LintException, TimeoutException {
		LSContext entity = PlintRobot.getInstance().getContext(name);
		String old = entity.name;
		render(entity, old);
	}

	public static void delete(String name) throws LintException, TimeoutException {
		PlintRobot.getInstance().deleteContext(name);
		index();
	}

	public static void save(@Valid LSContext entity) throws LintException, TimeoutException {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@create", entity);
		}
		// Send create request
		PlintRobot.getInstance().createContext(entity.name, entity.activationUrl, entity.url, entity.description, null);
		doFlashSuccess("Context created");
		index();
	}

	public static void update(@Valid LSContext entity, String old) throws LintException, TimeoutException {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}

		// Send Update request
		try {
			PlintRobot.getInstance().updateContext(entity.name, entity.activationUrl, entity.url, entity.description, old);
		} catch (LintException | TimeoutException e) {
			flash.error(e.getLocalizedMessage());
			render("@edit", entity);
		}

		doFlashSuccess("Context updated");
		index();
	}
}
