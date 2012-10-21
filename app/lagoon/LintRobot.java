package lagoon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import models.Permission;

import org.jboss.netty.handler.codec.http.HttpMethod;

import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.mvc.Http.Request;
import utils.LintConf;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import exceptions.LintException;

/**
 * LintRobot
 * 
 * @author linda.velte
 * 
 */
public class LintRobot {

	/**
	 * check if the current user has access to request controller and action with context
	 * 
	 * @param request
	 * @return true if is
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static boolean checkRequest(Request request, Long userid, String context) throws LintException, TimeoutException {
		// SanityCheck
		if (!checkConnection()) {
			return false;
		}

		// Verify access for controller and action
		return checkActionAccess(request.action, userid, context) ? true : false;
	}

	public static boolean checkRequest(Request request, Long userid) throws LintException, TimeoutException {
		return checkRequest(request, userid, null);
	}

	/**
	 * verify if lint can connect to remote Lagoon successfully
	 * 
	 * @return true if OK otherwise false
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static boolean checkConnection() throws LintException, TimeoutException {
		return sanityCheck();
	}

	/**
	 * User Login on Lagoon
	 * 
	 * @return Object userId if success otherwise null
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static Object login(String user, String password, String context) throws LintException, TimeoutException {
		return LintUser.login(user, password, context);
	}

	public static Object login(String user, String password) throws LintException, TimeoutException {
		return login(user, password, null);
	}

	/**
	 * User Logout on Lagoon with context
	 * 
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static boolean logout(Long userid, String context) throws LintException, TimeoutException {
		return LintUser.logout(userid, context);
	}

	public static boolean logout(Long userid) throws LintException, TimeoutException {
		return logout(userid, null);
	}

	// **************** Application Management Methods ***************************
	/**
	 * Get application info including modules
	 * 
	 * @return jsonelement containing all information
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement showApp() throws LintException, TimeoutException {
		return LintApp.showApp();
	}

	// **************** Profile Management Methods ***************************
	/**
	 * Return all active profiles on context
	 * 
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getProfiles(String context) throws LintException, TimeoutException {
		return LintProfiles.getProfiles(context);
	}

	public static JsonElement getProfiles() throws LintException, TimeoutException {
		return getProfiles(null);
	}

	// **************** User Management Methods ***************************

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
		return LintUser.createUser(login, email, name, context);
	}

	public static JsonElement createUser(String login, String email, String name) throws LintException, TimeoutException {
		return createUser(login, email, name, null);
	}

	/**
	 * Create User on Lagoon
	 * 
	 * @param login
	 * @param email
	 * @param name
	 * @param ghost
	 * @param notify
	 * @param profiles
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement createUser(String login, String email, String name, boolean ghost, String[] profiles, String context) throws LintException, TimeoutException {
		return LintUser.createUser(login, email, name, ghost, profiles, context);
	}

	public static JsonElement createUser(String login, String email, String name, boolean ghost, String[] profiles) throws LintException, TimeoutException {
		return createUser(login, email, name, ghost, profiles, null);
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
		return LintUser.registerUser(password, token, context);
	}

	public static JsonElement registerUser(String password, String token) throws LintException, TimeoutException {
		return registerUser(password, token, null);
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
		return LintUser.activateUser(userid, context);
	}

	public static JsonElement activateUser(Long userid) throws LintException, TimeoutException {
		return activateUser(userid, null);
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
		return LintUser.deactivateUser(userid, context);
	}

	public static JsonElement deactivateUser(Long userid) throws LintException, TimeoutException {
		return deactivateUser(userid);
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
		return LintUser.reactivateUser(userid, context);
	}

	public static JsonElement reactivateUser(Long userid) throws LintException, TimeoutException {
		return reactivateUser(userid, null);
	}

	/**
	 * Password Recovery
	 * 
	 * @param login
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement passwordRecovery(String email, String context) throws LintException, TimeoutException {
		return LintUser.passwordRecovery(email, context);
	}

	public static JsonElement passwordRecovery(String email) throws LintException, TimeoutException {
		return passwordRecovery(email, null);
	}

	/**
	 * List all users (actives or not) on application
	 * 
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getUsers(String context) throws LintException, TimeoutException {
		return LintUser.getUsers(context);
	}

	public static JsonElement getUsers() throws LintException, TimeoutException {
		return getUsers(null);
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
		return LintUser.showUser(userid, context);
	}

	public static JsonElement showUser(Long userid) throws LintException, TimeoutException {
		return showUser(userid, null);
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
		return LintUser.updateUser(userid, login, email, name, ghost, profiles, context);
	}

	public static JsonElement updateUser(Long userid, String login, String email, String name, boolean ghost, String[] profiles) throws LintException, TimeoutException {
		return updateUser(userid, login, email, name, ghost, profiles, null);
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
		return LintUser.updateUser(userid, login, email, name, context);
	}

	public static JsonElement updateUser(Long userid, String login, String email, String name) throws LintException, TimeoutException {
		return updateUser(userid, login, email, name, null);
	}

	/**
	 * delete permanently a user
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws TimeoutException
	 * @throws LintException
	 */
	public static JsonElement deleteUser(Long userid, String context) throws LintException, TimeoutException {
		return LintUser.deleteUser(userid, context);
	}

	public static JsonElement deleteUser(Long userid) throws LintException, TimeoutException {
		return deleteUser(userid, null);
	}

	// **************** Permission Management Methods ***************************

	/**
	 * Retrieves all permissions of an users
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getPermissions(Long userid, String context) throws LintException, TimeoutException {
		return LintPermissions.getPermissions(userid, context);
	}

	public static JsonElement getPermissions(Long userid) throws LintException, TimeoutException {
		return getPermissions(userid, null);
	}

	// **************** Context Management Methods ***************************
	/**
	 * List current contexts on application
	 * 
	 * @return {@link JsonElement} with existing contexts info
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getContexts() throws LintException, TimeoutException {
		return LintContext.getContexts();
	}

	/**
	 * Create context(context)
	 * 
	 * @param name
	 * @param activationUrl
	 * @param url
	 * @param description
	 * @param copyContext
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement createContext(String name, String activationUrl, String url, String description, String copyContext) throws LintException, TimeoutException {
		return LintContext.createContext(name, activationUrl, url, description, copyContext);
	}

	/**
	 * Update context(context)
	 * 
	 * @param newName
	 *            - new context
	 * @param description
	 *            - new context description
	 * @param oldName
	 *            - current context (context to be updated)
	 * @return {@link JsonElement} with updated context
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement updateContext(String newName, String activationUrl, String url, String description, String oldName) throws LintException, TimeoutException {
		return LintContext.updateContext(newName, activationUrl, url, description, oldName);
	}

	/**
	 * Get context information
	 * 
	 * @param context
	 *            - Context
	 * @return {@link JsonElement} with Context information
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement showContext(String context) throws LintException, TimeoutException {
		return LintContext.showContext(context);
	}

	/**
	 * Delete subdoamin/context
	 * 
	 * @param context
	 *            - Context to be deleted
	 * @return {@link JsonElement}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement deleteContext(String context) throws LintException, TimeoutException {
		return LintContext.deleteContext(context);
	}

	// **************** Utils ***************************
	/**
	 * sanitycheck functionality with version
	 * 
	 * @return true if OK otherwise false
	 * @throws LintException
	 * @throws TimeoutException
	 */
	private static boolean sanityCheck() throws LintException, TimeoutException {
		try {
			HttpResponse response = sendRequest("sanitycheck", null, LintConf.CONTENT_TYPE, HttpMethod.GET);
			String message = response.getJson().getAsJsonObject().get("message").getAsString();
			if (response.getJson().getAsJsonObject().get("sanity").getAsBoolean()) {
				Logger.debug("PLint - " + message);
				return true;
			} else {
				Logger.error("PLint - Your application is not working with Lagoon - " + message);
				return false;
			}
		} catch (LintException e) {
			Logger.error("PLint - " + e.toString());
			return false;
		}
	}

	/**
	 * generic request maker using WS. All calls to remote Lagoon should use this method
	 * 
	 * @param partialUrl
	 * @param body
	 * @param contentType
	 * @return response
	 * @throws LintException
	 */
	protected static HttpResponse sendRequest(String partialUrl, String body, String contentType, HttpMethod method) throws LintException, TimeoutException {
		WSRequest request = WS.url(LintConf.URL + partialUrl);
		request.headers.put("accept", "application/" + contentType);
		request.authenticate(LintConf.LOGIN, LintConf.PASSWORD);
		request.body(body);
		Logger.debug(partialUrl);
		HttpResponse response = null;

		if (method.equals(HttpMethod.GET)) {
			response = request.get();
		} else if (method.equals(HttpMethod.POST)) {
			response = request.post();
		} else if (method.equals(HttpMethod.PUT)) {
			response = request.put();
		} else if (method.equals(HttpMethod.DELETE)) {
			response = request.delete();
		} else {
			response = request.get();
		}

		Logger.debug("Lint - " + request.url);
		return checkResponseCode(response);
	}

	/**
	 * check if {@link HttpResponse} code is 2x
	 * 
	 * @param response
	 * @return
	 * @throws LintException
	 */
	private static HttpResponse checkResponseCode(HttpResponse response) throws LintException {
		if (response == null || !response.success()) {
			Logger.debug("Lint - " + response.getJson().toString());
			JsonObject jsonObject = response.getJson().getAsJsonObject();
			throw new LintException(jsonObject.get("error") + " :: " + jsonObject.get("message"));
		}
		return response;
	}

	/**
	 * Check if a user has permission to access some Controller.Action
	 * 
	 * @param controlerAction
	 * @return true if OK otherwise false
	 * @throws LintException
	 * @throws TimeoutException
	 */
	private static boolean checkActionAccess(String controlerAction, Long userid, String context) throws LintException, TimeoutException {
		JsonElement elem = getPermissions(userid, context);

		ArrayList<String> perms = new ArrayList<String>();
		Gson gson = new Gson();
		Permission[] permissions = gson.fromJson(elem, models.Permission[].class);
		for (Permission permission : permissions) {
			Map<String, List<String>> acMap = permission.getAction_aps();
			for (String action : acMap.keySet()) {
				for (String ap : acMap.get(action)) {
					perms.add(action + "." + ap);
				}
			}
		}
		Logger.debug(Arrays.asList(perms).toString());
		if (perms.contains(controlerAction)) {
			return true;
		} else {
			return false;
		}
	}
}
