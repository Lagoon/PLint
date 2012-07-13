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
		if(User.count() == 0 && !"test".equals(Play.id)) {

			//Create a new context for administration area
			String profiles[] = {"admin"};
			JsonElement resp = LintRobot.createContext("admin", "", "", "administration area", false);
			Logger.info("Context admin successfully created");
			Gson gson = new Gson();
			Context admin = gson.fromJson(resp, Context.class);
			admin.save();

			//Get Application contexts
			resp = LintRobot.getContexts();
			Context[] appContexts = gson.fromJson(resp, Context[].class);
			for(Context c : appContexts) {
				if(Context.findByName(c.name) == null) {
					c.save();
				}
			}

			//Create admin user on context admin
			resp = LintRobot.createUser("admin", "linda.velte@xlm.pt", "Administrator", false, profiles, "admin");
			UserLagoon userLagoon = UserLagoon.findByExternalID(resp.getAsJsonObject().get("id").getAsLong());
			User user = new User(userLagoon, admin);
			user.save();
			Logger.info("User admin successfully created");
		}
	}
}