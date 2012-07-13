package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import lagoon.LintRobot;
import models.Profile;
import models.User;
import models.UserLagoon;
import play.data.validation.Valid;
import play.i18n.Messages;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import exceptions.LintException;

/**
 * Users
 * @author linda.velte
 *
 */
public class Users extends Application {

	public static void index() {
		List<User> entities = models.User.all().fetch();
		render(entities);
	}

	public static void indexByContext() throws LintException, TimeoutException {
		List<User> entities = new ArrayList<User>();

		JsonElement resp = LintRobot.getUsers(Lintity.currentContext().name);
		Gson gson = new Gson();
		UserLagoon[] users  = gson.fromJson(resp, UserLagoon[].class);

		for(UserLagoon u : users) {
			UserLagoon userLagoon = UserLagoon.findByExternalID(u.id);
			User user = User.findByUserLagoon(userLagoon);
			entities.add(user);
		}
		render(entities);
	}

	public static void showUser(Long id ) throws LintException, TimeoutException {
		User entity = User.findById(id);
		JsonElement resp = LintRobot.showUser(entity.userLagoon.id, Lintity.currentContext().name);
		Gson gson = new Gson();
		UserLagoon userLagoon = gson.fromJson(resp, UserLagoon.class);
		UserLagoon find = UserLagoon.findByExternalID(userLagoon.id);
		entity = User.findByUserLagoon(find);
		render(entity);
	}

	public static void create(User entity) {
		render(entity);
	}

	public static void show(Long id) {
		User entity = User.findById(id);
		render(entity);
	}

	public static void deactivate(Long id) throws TimeoutException, LintException {
		User user = User.findById(id);
		LintRobot.deactivateUser(user.userLagoon.id, user.context.name);
		doFlashSuccess("User deactivated");
		index();
	}

	public static void activate(Long id) throws LintException, TimeoutException {
		User user = User.findById(id);
		LintRobot.activateUser(user.userLagoon.id, user.context.name);
		doFlashSuccess("User activated");
		index();
	}

	public static void reactivate(Long id) throws LintException, TimeoutException   {
		User user = User.findById(id);
		LintRobot.reactivateUser(user.userLagoon.id, user.context.name);
		doFlashSuccess("User reactivated");
		index();
	}

	public static void edit(java.lang.Long id) throws LintException, TimeoutException {
		User entity = User.findById(id);

		//get context profiles
		JsonElement resp = LintRobot.getProfiles(Lintity.currentContext().name);
		Gson gson = new Gson();
		Profile[] profilesLagoon  = gson.fromJson(resp, Profile[].class);

		for(Profile p : profilesLagoon) {
			Profile userProfile = Profile.findByExternalId(p.id);
			if(userProfile == null) {
				p.save();
			}
		}
		List<Profile> profiles = Profile.all().fetch();
		render(entity, profiles);
	}


	public static void update(@Valid User entity) throws LintException, TimeoutException {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}

		List<Profile> profiles = new ArrayList<Profile>(entity.profiles);
		String[] updateProfiles = new String[profiles.size()];
		for(int i = 0; i < profiles.size(); i++) {
			updateProfiles[i] = profiles.get(i).name;
		}

		JsonElement resp = LintRobot.updateUser(entity.userLagoon.id, entity.userLagoon.login, entity.userLagoon.email, entity.userLagoon.name, false, updateProfiles, Lintity.currentContext().name);
		Gson gson = new Gson();


		UserLagoon u = gson.fromJson(resp, UserLagoon.class);
		UserLagoon old = UserLagoon.findByExternalID(u.id);
		old.name = u.name;
		old.login = u.login;
		old.email = u.email;

		JsonElement pjson = resp.getAsJsonObject().get("profiles").getAsJsonArray();
		Profile[] profilesLagoon  = gson.fromJson(pjson, Profile[].class);

		for(Profile p : profilesLagoon) {
			Profile myProfile = Profile.findByExternalId(p.id);
			entity.profiles.add(myProfile);
		}

		entity.userLagoon = old;
		entity.save();

		flash.success(Messages.get("scaffold.updated", "User"));
		indexByContext();
	}
}
