package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import lagoon.PlintRobot;
import ls.LSProfile;
import ls.LSUser;
import models.User;
import play.data.validation.Valid;
import play.i18n.Messages;
import exceptions.LintException;

/**
 * Users
 * 
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

		ArrayList<LSUser> users = PlintRobot.getInstance().getUsers(Lintity.currentContext());

		for (LSUser u : users) {
			User user = User.findByExternalID(u.id);
			entities.add(user);
		}
		render(entities);
	}

	public static void showUser(Long id) throws LintException, TimeoutException {
		User entity = User.findById(id);
		LSUser resp = PlintRobot.getInstance().getUser(entity.externalID, Lintity.currentContext());
		entity = User.findByExternalID(resp.id);
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
		PlintRobot.getInstance().deactivateUser(user.externalID, Lintity.currentContext());
		doFlashSuccess("User deactivated");
		index();
	}

	public static void delete(Long id) throws TimeoutException, LintException {
		User user = User.findById(id);
		PlintRobot.getInstance().deleteUser(user.externalID, Lintity.currentContext());
		doFlashSuccess("User deleted");
		index();
	}

	public static void activate(Long id) throws LintException, TimeoutException {
		User user = User.findById(id);
		PlintRobot.getInstance().activateUser(user.externalID, Lintity.currentContext());
		doFlashSuccess("User activated");
		index();
	}

	public static void reactivate(Long id) throws LintException, TimeoutException {
		User user = User.findById(id);
		PlintRobot.getInstance().reactivateUser(user.externalID, Lintity.currentContext());
		doFlashSuccess("User reactivated");
		index();
	}

	public static void edit(java.lang.Long id) throws LintException, TimeoutException {
		User entity = User.findById(id);
		notFoundIfNull(entity);

		// get context profiles
		ArrayList<LSProfile> profilesLagoon = PlintRobot.getInstance().getProfiles(Lintity.currentContext());
		render(entity, profilesLagoon);
	}

	public static void update(@Valid User entity) throws LintException, TimeoutException {
		if (validation.hasErrors()) {
			flash.error(Messages.get("scaffold.validation"));
			render("@edit", entity);
		}

		//		PlintRobot.getInstance().updateUser();
		//
		//
		//		List<Profile> profiles = new ArrayList<Profile>(entity.profiles);
		//		String[] updateProfiles = new String[profiles.size()];
		//		for (int i = 0; i < profiles.size(); i++) {
		//			updateProfiles[i] = profiles.get(i).name;
		//		}

		//		JsonElement resp = PlintRobot.getInstance().updateUser(entity.userLagoon.id, entity.userLagoon.login, entity.email, entity.name, false, updateProfiles, Lintity.currentContext().name);
		//		Gson gson = new Gson();
		//
		//		UserLagoon u = gson.fromJson(resp, UserLagoon.class);
		//		UserLagoon old = UserLagoon.findByExternalID(u.id);
		//		old.name = u.name;
		//		old.login = u.login;
		//		old.email = u.email;
		//
		//		JsonElement pjson = resp.getAsJsonObject().get("profiles").getAsJsonArray();
		//		Profile[] profilesLagoon = gson.fromJson(pjson, Profile[].class);
		//
		//		for (Profile p : profilesLagoon) {
		//			Profile myProfile = Profile.findByExternalId(p.id);
		//			entity.profiles.add(myProfile);
		//		}
		//
		//		entity.userLagoon = old;
		//		entity.save();

		flash.success(Messages.get("scaffold.updated", "User"));
		indexByContext();
	}
}
