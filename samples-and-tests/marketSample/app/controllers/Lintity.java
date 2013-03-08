package controllers;

import helpers.subdomainchecker.SubdomainChecker;

import java.util.concurrent.TimeoutException;

import lagoon.PlintRobot;
import ls.LSUser;
import models.User;
import play.data.validation.Required;
import exceptions.LintException;

public class Lintity extends SecureLint.Lintity {

	public static void registerUser(@Required String username, @Required String email, @Required String name) {

		String context = SubdomainChecker.currentSubdomain(request);

		LSUser resp = null;

		//Send user create request
		try {
			resp = PlintRobot.getInstance().createUser(username, email, name, context);
		} catch(Exception ex) {

			Application.doFlashError(ex.getMessage());
			renderTemplate("SecureLint/Lintity/register.html");
		}


		//save user profiles
		User user = new User();
		user.externalID = resp.id;
		user.save();
		flash.success("User successfully created. View your email inbox.");
		renderTemplate("SecureLint/login.html");
	}

	public static void activateUser(@Required String password, @Required String passwordConf, String token) throws Exception {
		if(!password.equals(passwordConf)) {
			Application.doFlashError("Passwords don't match");
			renderTemplate("SecureLint/Lintity/activation.html", token);
		}

		String context = SubdomainChecker.currentSubdomain(request);
		PlintRobot.getInstance().registerUser(password, token, context);
		flash.success("User successfully registered.");
		renderTemplate("SecureLint/login.html");
	}

	public static void resendPassword(@Required String email) {
		String context = SubdomainChecker.currentSubdomain(request);
		try{
			PlintRobot.getInstance().passwordRecovery(email, context);
			flash.success("Password successfully resended.");
		}catch (LintException | TimeoutException ex){
			Application.doFlashError(ex.getMessage());
			renderTemplate("SecureLint/Lintity/password_recovery.html");
		}
		renderTemplate("SecureLint/login.html");
	}
}
