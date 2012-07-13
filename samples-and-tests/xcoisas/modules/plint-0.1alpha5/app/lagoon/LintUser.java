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
 * @author linda.velte
 *
 */
public class LintUser extends LintRobot{

	/**
	 * User Login on Lagoon
	 * 
	 * @return Object userId if success otherwise null
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static Object login(String user, String password, String context) throws LintException, TimeoutException{

		// SanityCheck
		if(!checkConnection()){
			return false;
		}

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", user);
		bodyObj.addProperty("password", password);

		String url;
		if(context == null){
			url = "applications/" + identifier() + "/users/login";
		}
		else{
			url = "applications/" + identifier() + "/contexts/" + context + "/users/login";
			Logger.debug("URL : " + url);
		}
		// send request
		HttpResponse resp = sendRequest(url, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST);

		if(resp.getStatus() == 200){
			// "Saves" user Id
			Long userID = resp.getJson().getAsJsonObject().get("id").getAsLong();

			UserLagoon userLagoon = UserLagoon.findByExternalID(userID);

			//get user permissions
			if(context == null) {
				url = "applications/" + identifier() + "/users/" + userID + "/permissions";
			}
			else{
				url = "applications/" + identifier() + "/contexts/" + context + "/users/" + userID +  "/permissions";
				Logger.debug("URL : " + url);
			}
			// send request
			resp = sendRequest(url, null, LintConf.CONTENT_TYPE, HttpMethod.GET);

			Gson gson = new Gson();
			Object[] permissions  = gson.fromJson(resp.getJson(), Permission[].class);

			for (Object permission : permissions) {
				Map<String, List<String>> acMap = ((Permission) permission).getAction_aps();
				for (String action : acMap.keySet()) {
					for (String ap : acMap.get(action)) {

						UserPermission p = UserPermission.findByActionAndActionPoint(action, ap);
						if(p == null) {
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
	public static boolean logout(Long userID, String context) throws LintException, TimeoutException{

		String url;
		if(context == null){
			url = "applications/" + identifier() + "/users/" + userID + "/logout";
		}
		else{
			url = "applications/" + identifier() + "/contexts/" + context + "/users/" + userID + "/logout";
		}
		// send request
		sendRequest(url, null, LintConf.CONTENT_TYPE,HttpMethod.GET);

		return true;
	}

	/**
	 * Create User on Lagoon (just creating guest users...)
	 * @param login
	 * @param email
	 * @param name
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement createUser(String login, String email, String name, String context) throws LintException, TimeoutException{

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login);
		bodyObj.addProperty("email", email);
		bodyObj.addProperty("name", name);

		String partialUrl;
		if(context == null || context.isEmpty()){
			partialUrl = "applications/" + identifier() + "/users";
		}
		else{
			partialUrl = "applications/" + identifier() + "/contexts/" + context + "/users";
		}

		//send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST);


		Gson gson = new Gson();
		UserLagoon userLagoon  = gson.fromJson(resp.getJson(), UserLagoon.class);
		userLagoon.save();
		return resp.getJson();
	}

	/**
	 * Create User on Lagoon
	 * @param login
	 * @param email
	 * @param name
	 * @param ghost
	 * @param profiles
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement createUser(String login, String email, String name, boolean ghost, String[] profiles, String context) throws LintException, TimeoutException{

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login);
		bodyObj.addProperty("email", email);
		bodyObj.addProperty("name", name);
		bodyObj.addProperty("ghost", ghost);
		if(profiles!=null){
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
		if(context == null){
			partialUrl = "applications/" + identifier() + "/users";
		}
		else{
			partialUrl = "applications/" + identifier() + "/contexts/"+context+"/users";
		}

		//send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST);

		Gson gson = new Gson();
		UserLagoon userLagoon  = gson.fromJson(resp.getJson(), UserLagoon.class);
		userLagoon.save();
		return resp.getJson();
	}

	/**
	 * Register user on Lagoon
	 * @param password
	 * @param token
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement registerUser(String password, String token, String context) throws LintException, TimeoutException{

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("token", token);
		bodyObj.addProperty("password", password);

		String partialUrl;
		if(context == null){
			partialUrl = "applications/" + identifier() + "/users/register";
		}
		else{
			partialUrl = "applications/" + identifier() + "/contexts/" + context + "/users/register";
		}
		//send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST);
		return resp.getJson();
	}

	/**
	 * Activate user on Lagoon
	 * @param userLagoonId
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement activateUser(Long userLagoonId, String context) throws LintException, TimeoutException{

		String partialUrl;
		if(context == null){
			partialUrl = "applications/" + identifier() + "/users/" + userLagoonId + "/activate";
		}
		else{
			partialUrl = "applications/" + identifier() + "/contexts/" + context + "/users/" + userLagoonId + "/activate";
		}
		//send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * Deactivate user on Lagoon
	 * @param userLagoonId
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement deactivateUser(Long userLagoonId, String context) throws LintException, TimeoutException{

		String partialUrl;
		if(context == null) {
			partialUrl = "applications/" + identifier() + "/users/" + userLagoonId + "/deactivate";
		}
		else {
			partialUrl = "applications/" + identifier() + "/contexts/" + context + "/users/" + userLagoonId + "/deactivate";
		}
		//send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * Reactivate user on Lagoon
	 * @param userLagoonId
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement reactivateUser(Long userLagoonId, String context) throws LintException, TimeoutException{

		String partialUrl;
		if(context == null) {
			partialUrl = "applications/" + identifier() + "/users/" + userLagoonId + "/reactivate";
		}
		else {
			partialUrl = "applications/" + identifier() + "/contexts/" + context + "/users/" + userLagoonId + "/reactivate";
		}
		//send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * List all users (active or not) on application
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getUsers(String context) throws LintException, TimeoutException{

		String partialUrl;
		if(context == null){
			partialUrl = "applications/" + identifier() + "/users";
		}
		else{
			partialUrl = "applications/" + identifier() + "/contexts/" + context + "/users";
		}
		//send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * List information of the designated user
	 * @param idUserLagoon
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement showUser(Long idUserLagoon, String context) throws LintException, TimeoutException{

		String partialUrl;
		if(context == null){
			partialUrl = "applications/" + identifier() + "/users/" + idUserLagoon;
		}
		else{
			partialUrl = "applications/" + identifier() + "/contexts/" + context + "/users/" + idUserLagoon;
		}
		//send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * Update a designated user
	 * @param idUserLagoon
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
	public static JsonElement updateUser(Long idUserLagoon, String login, String email, String name, boolean ghost, String[] profiles, String context) throws LintException, TimeoutException{

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login);
		bodyObj.addProperty("email", email);
		bodyObj.addProperty("name", name);
		bodyObj.addProperty("ghost", ghost);

		if(profiles != null) {
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
		if(context == null){
			partialUrl = "applications/" + identifier() + "/users/" + idUserLagoon;
		}
		else{
			partialUrl = "applications/" + identifier() + "/contexts/" + context + "/users/" + idUserLagoon;
		}

		//send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.PUT);
		return resp.getJson();
	}

	/**
	 * Update a designated user (with mandatory args only)
	 * @param idUserLagoon
	 * @param login
	 * @param email
	 * @param name
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement updateUser(Long idUserLagoon, String login, String email, String name, String context) throws LintException, TimeoutException{

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login);
		bodyObj.addProperty("email", email);
		bodyObj.addProperty("name", name);

		String partialUrl;
		if(context == null){
			partialUrl = "applications/" + identifier() + "/users/" + idUserLagoon;
		}
		else{
			partialUrl = "applications/" + identifier() + "/contexts/" + context + "/users/" + idUserLagoon;
		}

		//send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.PUT);
		return resp.getJson();
	}

	// ***************************************************************
}
