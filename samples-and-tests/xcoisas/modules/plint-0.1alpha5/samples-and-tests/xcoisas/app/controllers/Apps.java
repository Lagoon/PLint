package controllers;

import lagoon.LintRobot;

import com.google.gson.JsonElement;



public class Apps extends Application {

	public static void show() throws Exception {
		JsonElement resp = LintRobot.showApp();

		String name = resp.getAsJsonObject().get("application").getAsJsonObject().get("name").getAsString();
		String description = resp.getAsJsonObject().get("application").getAsJsonObject().get("description").getAsString();
		render(name, description);
	}

}
