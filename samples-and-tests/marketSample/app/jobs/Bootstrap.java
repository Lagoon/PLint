package jobs;

import java.util.ArrayList;
import models.User;
import lagoon.PlintRobot;
import ls.LSContext;
import play.Logger;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;


@OnApplicationStart
public class Bootstrap extends Job {
	@Override
	public void doJob() throws Exception {
		if (User.count() == 0 && !"test".equals(Play.id)) {

//			// Get Application contexts
//			JsonElement resp = LintRobot.getContexts();
//			Gson gson = new Gson();
//			Context[] appContexts = gson.fromJson(resp, Context[].class);
//			for (Context c : appContexts) {
//				if (Context.findByName(c.name) == null) {
//					c.save();
//				}
//
//				// get context users
//				resp = LintRobot.getUsers(c.name);
//				UserLagoon[] users = gson.fromJson(resp, UserLagoon[].class);
//
//				for (UserLagoon u : users) {
//					User user = new User(u, c);
//					user.save();
//				}
//			}
			
			ArrayList<LSContext> contexts = PlintRobot.getInstance().getContexts();
			
			boolean exist = false;
			for (LSContext ctx : contexts) {
				if("admin".equals(ctx.name)){
					exist = true;
				}
			}

			// If admin does not exist create context

//			Context admin = Context.findByName("admin");
			if (!exist) {
				String profiles[] = { "admin" };
				PlintRobot.getInstance().createContext("admin", "", "", "administration area", null);
				Logger.info("Context admin successfully created");

//				admin = gson.fromJson(resp, Context.class);
//				admin.save();

				PlintRobot.getInstance().createUser("admin", "lagoon@xlm.pt", "Administrator", false, profiles, "admin");
//				UserLagoon userLagoon = UserLagoon.findByExternalID(resp.getAsJsonObject().get("id").getAsLong());
//				User user = new User(userLagoon, admin);
//				user.save();
				Logger.info("User admin successfully created");
			}
		}
	}
}