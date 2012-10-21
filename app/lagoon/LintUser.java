package lagoon;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import models.Permission;
import models.UserLagoon;
import models.UserPermission;

import org.jboss.netty.handler.codec.http.HttpMethod;

import play.Logger;
import play.libs.WS.HttpResponse;
import utils.LintConf;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import exceptions.LintException;

/**
 * User Management Methods
 * 
 * @author linda.velte
 * 
 */
public class LintUser extends LintRobot {

	/**
	 * User Login on Lagoon
	 * 
	 * @return Object userId if success otherwise null
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static Object login(String login, String password, String context) throws LintException, TimeoutException {

		// SanityCheck
		if (!checkConnection()) {
			return false;
		}

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login);
		bodyObj.addProperty("password", password);

		String url;
		if (context == null) {
			url = "users/login";
		} else {
			url = "contexts/" + context + "/users/login";
			Logger.debug("URL : " + url);
		}
		// send request
		HttpResponse resp = sendRequest(url, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST);

		if (resp.getStatus() == 200) {
			// "Saves" user Id
			Long userID = resp.getJson().getAsJsonObject().get("id").getAsLong();

			UserLagoon userLagoon = UserLagoon.findByExternalID(userID);

			// get user permissions
			if (context == null) {
				url = "users/" + userID + "/permissions";
			} else {
				url = "contexts/" + context + "/users/" + userID + "/permissions";
				Logger.debug("URL : " + url);
			}
			// send request
			resp = sendRequest(url, null, LintConf.CONTENT_TYPE, HttpMethod.GET);

			Gson gson = new Gson();
			Object[] permissions = gson.fromJson(resp.getJson(), Permission[].class);

			for (Object permission : permissions) {
				Map<String, List<String>> acMap = ((Permission) permission).getAction_aps();
				for (String action : acMap.keySet()) {
					for (String ap : acMap.get(action)) {

						UserPermission p = UserPermission.findByActionAndActionPoint(action, ap);
						if (p == null) {
							p = new UserPermission(action, ap);
							p.save();
						}
						userLagoon.userAccessPermission.add(p);
					}
				}
			}
			userLagoon.save();
			return userID;
		}
		return null;
	}

	/**
	 * User Logout on Lagoon
	 * 
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static boolean logout(Long userid, String context) throws LintException, TimeoutException {

		String url;
		if (context == null) {
			url = "users/" + userid + "/logout";
		} else {
			url = "contexts/" + context + "/users/" + userid + "/logout";
		}
		// send request
		sendRequest(url, null, LintConf.CONTENT_TYPE, HttpMethod.GET);

		return true;
	}

	/**
	 * Create User on Lagoon (just creating guest users...)
	 * 
	 * @param login
	 * @param email
	 * @param name
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement createUser(String login, String email, String name, String context) throws LintException, TimeoutException {

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login);
		bodyObj.addProperty("email", email);
		bodyObj.addProperty("name", name);

		String partialUrl;
		if (context == null || context.isEmpty()) {
			partialUrl = "users";
		} else {
			partialUrl = "contexts/" + context + "/users";
		}

		// send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST);

		Gson gson = new Gson();
		UserLagoon userLagoon = gson.fromJson(resp.getJson(), UserLagoon.class);
		userLagoon.save();
		return resp.getJson();
	}

	/**
	 * Create User on Lagoon
	 * 
	 * @param login
	 * @param email
	 * @param name
	 * @param ghost
	 * @param profiles
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement createUser(String login, String email, String name, boolean ghost, String[] profiles, String context) throws LintException, TimeoutException {

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login);
		bodyObj.addProperty("email", email);
		bodyObj.addProperty("name", name);
		bodyObj.addProperty("ghost", ghost);
		if (profiles != null) {
			JsonObject profileObj = new JsonObject();
			JsonArray arr = new JsonArray();
			for (String profile : profiles) {
				profileObj.addProperty("name", profile);
				arr.add(profileObj);
			}
			bodyObj.add("profiles", arr);
		}
		Logger.debug(bodyObj.toString());

		String partialUrl;
		if (context == null) {
			partialUrl = "users";
		} else {
			partialUrl = "contexts/" + context + "/users";
		}

		// send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST);

		Gson gson = new Gson();
		UserLagoon userLagoon = gson.fromJson(resp.getJson(), UserLagoon.class);
		userLagoon.save();
		return resp.getJson();
	}

	/**
	 * Register user on Lagoon
	 * 
	 * @param password
	 * @param token
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement registerUser(String password, String token, String context) throws LintException, TimeoutException {

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("token", token);
		bodyObj.addProperty("password", password);

		String partialUrl;
		if (context == null) {
			partialUrl = "users/register";
		} else {
			partialUrl = "contexts/" + context + "/users/register";
		}
		// send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST);
		return resp.getJson();
	}

	/**
	 * Activate user on Lagoon
	 * 
	 * @param userid
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement activateUser(Long userid, String context) throws LintException, TimeoutException {

		String partialUrl;
		if (context == null) {
			partialUrl = "users/" + userid + "/activate";
		} else {
			partialUrl = "contexts/" + context + "/users/" + userid + "/activate";
		}
		// send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * Deactivate user on Lagoon
	 * 
	 * @param userid
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement deactivateUser(Long userid, String context) throws LintException, TimeoutException {

		String partialUrl;
		if (context == null) {
			partialUrl = "users/" + userid + "/deactivate";
		} else {
			partialUrl = "contexts/" + context + "/users/" + userid + "/deactivate";
		}
		// send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * Reactivate user on Lagoon
	 * 
	 * @param userid
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement reactivateUser(Long userid, String context) throws LintException, TimeoutException {

		String partialUrl;
		if (context == null) {
			partialUrl = "users/" + userid + "/reactivate";
		} else {
			partialUrl = "contexts/" + context + "/users/" + userid + "/reactivate";
		}
		// send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * List all users (active or not) on application
	 * 
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getUsers(String context) throws LintException, TimeoutException {

		String partialUrl;
		if (context == null) {
			partialUrl = "users";
		} else {
			partialUrl = "contexts/" + context + "/users";
		}
		// send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * List information of the designated user
	 * 
	 * @param userid
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement showUser(Long userid, String context) throws LintException, TimeoutException {

		String partialUrl;
		if (context == null) {
			partialUrl = "users/" + userid;
		} else {
			partialUrl = "contexts/" + context + "/users/" + userid;
		}
		// send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * List information of the designated user
	 * 
	 * @param userid
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement deleteUser(Long userid, String context) throws LintException, TimeoutException {

		String partialUrl;
		if (context == null) {
			partialUrl = "users/" + userid;
		} else {
			partialUrl = "contexts/" + context + "/users/" + userid;
		}
		// send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.DELETE);
		return resp.getJson();
	}

	/**
	 * User password recovery
	 * 
	 * @param login
	 * @param context
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement passwordRecovery(String email, String context) throws LintException, TimeoutException {

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("email", email);

		String partialUrl;
		if (context == null) {
			partialUrl = "users/password_recovery";
		} else {
			partialUrl = "contexts/" + context + "/users/password_recovery";
		}
		// send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST);
		return resp.getJson();
	}

	/**
	 * Update a designated user
	 * 
	 * @param userid
	 * @param login
	 * @param email
	 * @param name
	 * @param ghost
	 * @param notify
	 * @param profiles
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement updateUser(Long userid, String login, String email, String name, boolean ghost, String[] profiles, String context) throws LintException, TimeoutException {

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login);
		bodyObj.addProperty("email", email);
		bodyObj.addProperty("name", name);
		bodyObj.addProperty("ghost", ghost);

		if (profiles != null) {
			JsonArray arr = new JsonArray();
			for (String profile : profiles) {
				JsonObject profileObj = new JsonObject();
				profileObj.addProperty("name", profile);
				arr.add(profileObj);
			}
			bodyObj.add("profiles", arr);
		}
		Logger.debug(bodyObj.toString());

		String partialUrl;
		if (context == null) {
			partialUrl = "users/" + userid;
		} else {
			partialUrl = "contexts/" + context + "/users/" + userid;
		}

		// send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.PUT);
		return resp.getJson();
	}

	/**
	 * Update a designated user (with mandatory args only)
	 * 
	 * @param userid
	 * @param login
	 * @param email
	 * @param name
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement updateUser(Long userid, String login, String email, String name, String context) throws LintException, TimeoutException {

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login);
		bodyObj.addProperty("email", email);
		bodyObj.addProperty("name", name);

		String partialUrl;
		if (context == null) {
			partialUrl = "users/" + userid;
		} else {
			partialUrl = "contexts/" + context + "/users/" + userid;
		}

		// send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.PUT);
		return resp.getJson();
	}

	// ***************************************************************
}
