package controllers;

import lagoon.PlintRobot;
import ls.LSInstance;

public class Apps extends Application {

	public static void show() throws Exception {
		LSInstance resp = PlintRobot.getInstance().getInstanceInfo();

		String name = resp.application.name;
		String description = resp.application.description;
		render(name, description);
	}

}
