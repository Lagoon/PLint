package jobs;

import lagoon.LintRobot;
import models.Context;
import models.User;
import models.UserLagoon;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

@OnApplicationStart
public class Bootstrap extends Job {
	@Override
	public void doJob() throws Exception {
		if (User.count() == 0 && !"test".equals(Play.id)) {

			// Get Application contexts
			JsonElement resp = LintRobot.getContexts();
			Gson gson = new Gson();
			Context[] appContexts = gson.fromJson(resp, Context[].class);
			for (Context c : appContexts) {
				if (Context.findByName(c.name) == null) {
					c.save();
				}

				// get context users
				resp = LintRobot.getUsers(c.name);
				UserLagoon[] users = gson.fromJson(resp, UserLagoon[].class);

				for (UserLagoon u : users) {
					User user = new User(u, c);
					user.save();
				}
			}

			// If admin does not exist create context

			Context admin = Context.findByName("admin");
			if (admin == null) {
				String profiles[] = { "admin" };
				resp = LintRobot.createContext("admin", "", "", "administration area", null);
				Logger.info("Context admin successfully created");

				admin = gson.fromJson(resp, Context.class);
				admin.save();

				resp = LintRobot.createUser("admin", "lagoon@xlm.pt", "Administrator", false, profiles, "admin");
				UserLagoon userLagoon = UserLagoon.findByExternalID(resp.getAsJsonObject().get("id").getAsLong());
				User user = new User(userLagoon, admin);
				user.save();
				Logger.info("User admin successfully created");
			}
		}
	}
}