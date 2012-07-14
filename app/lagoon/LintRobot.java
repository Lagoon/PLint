package lagoon;

import helpers.subdomainchecker.SubdomainChecker;

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
import play.mvc.Util;
import utils.LintConf;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import exceptions.LintException;

public class LintRobot {

	/**
	 * check if the current user has access to request controller and action
	 * 
	 * @param request
	 * @return true if is
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static boolean checkRequest(Request request, Long userID) throws LintException, TimeoutException {
		// SanityCheck
		if (!checkConnection()) {
			return false;
		}

		// Verify access for controller and action
		return checkTest(request.action, userID) ? true : false;
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

	/**
	 * User Logout on Lagoon
	 * 
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static boolean logout(Long userID, String context) throws LintException, TimeoutException {
		return LintUser.logout(userID, context);
	}

	// ***************************************************************

	// ~~~~~~~~~~~~~ Private ~~~~~~~~~~~~~

	/**
	 * sanitycheck functionality with version
	 * 
	 * @return true if OK otherwise false
	 * @throws LintException
	 * @throws TimeoutException
	 */
	private static boolean sanityCheck() throws LintException, TimeoutException {
		try {
			HttpResponse response = sendRequest("sanitycheck/" + LintConf.VERSION, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
			String message = response.getJson().getAsJsonObject().get("message").getAsString();
			if (response.getJson().getAsJsonObject().get("sanity").getAsBoolean()) {
				Logger.debug("Lint - " + message);
				return true;
			} else {
				Logger.error("Lint - Your application is not working with Lagoon - " + message);
				return false;
			}
		} catch (LintException e) {
			Logger.error("Lint - " + e.toString());
			return false;
		}
	}

	/**
	 * identifier functionality
	 * 
	 * @return true if OK otherwise false
	 * @throws LintException
	 * @throws TimeoutException
	 */
	protected static Long identifier() throws LintException, TimeoutException {
		HttpResponse response = sendRequest("identifier", null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		JsonObject jObj = response.getJson().getAsJsonObject();
		return jObj.get("id").getAsLong();
	}

	// ~~~~~~~~~~~~~ Utils ~~~~~~~~~~~~~~

	/**
	 * generic request maker using WS. All calls to remote Lagoon should use
	 * this method
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
	private static boolean checkTest(String controlerAction, Long userID) throws LintException, TimeoutException {

		ArrayList<String> array = getPermissions(userID);
		Logger.info(controlerAction);
		Logger.debug(Arrays.asList(array).toString());
		if (array.contains(controlerAction)) {
			return true;
		} else {
			return false;
		}
	}

	// **************** Application Management Methods
	// ***************************
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
	 * Return all active profiles
	 * 
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getProfiles(String context) throws LintException, TimeoutException {
		return LintProfiles.getProfiles(context);
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

	/**
	 * Activate user on Lagoon
	 * 
	 * @param userLagoonId
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement activateUser(Long userLagoonId, String context) throws LintException, TimeoutException {
		return LintUser.activateUser(userLagoonId, context);
	}

	/**
	 * Deactivate user on Lagoon
	 * 
	 * @param userLagoonId
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement deactivateUser(Long userLagoonId, String context) throws LintException, TimeoutException {
		return LintUser.deactivateUser(userLagoonId, context);
	}

	/**
	 * Reactivate user on Lagoon
	 * 
	 * @param userLagoonId
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement reactivateUser(Long userLagoonId, String context) throws LintException, TimeoutException {
		return LintUser.reactivateUser(userLagoonId, context);
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

	/**
	 * List information of the designated user
	 * 
	 * @param idUserLagoon
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement showUser(Long idUserLagoon, String context) throws LintException, TimeoutException {
		return LintUser.showUser(idUserLagoon, context);
	}

	/**
	 * Update a designated user
	 * 
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
	public static JsonElement updateUser(Long idUserLagoon, String login, String email, String name, boolean ghost, String[] profiles, String context) throws LintException, TimeoutException {
		return LintUser.updateUser(idUserLagoon, login, email, name, ghost, profiles, context);
	}

	/**
	 * Update a designated user (with mandatory args only)
	 * 
	 * @param idUserLagoon
	 * @param login
	 * @param email
	 * @param name
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement updateUser(Long idUserLagoon, String login, String email, String name, String context) throws LintException, TimeoutException {
		return LintUser.updateUser(idUserLagoon, login, email, name, context);
	}

	// **************** Permission Management Methods
	// ***************************
	public static JsonElement getPermissions(Long idUserLagoon, String context) throws LintException, TimeoutException {
		return LintPermissions.getPermissions(idUserLagoon, context);
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
	public static JsonElement createContext(String name, String activationUrl, String url, String description, boolean copyContext) throws LintException, TimeoutException {
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
	 * @param contextName
	 *            - Context
	 * @return {@link JsonElement} with Context information
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement showContext(String contextName) throws LintException, TimeoutException {
		return LintContext.showContext(contextName);
	}

	/**
	 * Delete subdoamin/context
	 * 
	 * @param contextName
	 *            - Context to be deleted
	 * @return {@link JsonElement}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement deleteContext(String contextName) throws LintException, TimeoutException {
		return LintContext.deleteContext(contextName);
	}

	// ***************************************************************

	// **************** Utils ***************************

	/**
	 * Get all user permissions
	 * 
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	@Util
	private static ArrayList<String> getPermissions(Long userID) throws LintException, TimeoutException {

		String context = SubdomainChecker.currentSubdomain(Request.current());
		JsonElement elem = getPermissions(userID, context);

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
		return perms;
	}
}
