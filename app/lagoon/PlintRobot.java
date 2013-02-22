package lagoon;

import helpers.LSHttpManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import lagoon.api.InstanceResource;
import lagoon.api.ContextResource;
import lagoon.api.PermissionResource;
import lagoon.api.ProfileResource;
import lagoon.api.UserResource;
import ls.LSContext;
import ls.LSInstance;
import ls.LSPermission;
import ls.LSProfile;
import ls.LSUser;

import org.jboss.netty.handler.codec.http.HttpMethod;

import play.Logger;
import play.cache.Cache;
import play.libs.WS.HttpResponse;
import play.mvc.Http.Request;
import utils.LintConf;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import exceptions.LintException;

/**
 * Lagoon Security Robot
 * 
 * @author dpeixoto
 *
 */
public class PlintRobot extends LSHttpManager implements InstanceResource, ContextResource, PermissionResource, ProfileResource, UserResource{

	private static PlintRobot instance = null;

	public static PlintRobot getInstance() {
		if(instance == null) {
			instance = new PlintRobot();
		}
		return instance;
	}

	private PlintRobot() {}

	/**
	 * checks if the current user has access to request controller and action with context
	 * 
	 * @param request
	 * @return true if is
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public boolean checkRequest(Request request, Long userid, String context) throws LintException, TimeoutException {
		return checkActionAccess(request.action, userid, context) ? true : false;
	}

	public boolean checkRequest(Request request, Long userid) throws LintException, TimeoutException {
		return checkRequest(request, userid, null);
	}

	@Override
	public LSUser login(String login, String password, String context) throws LintException, TimeoutException {

		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login.trim());
		bodyObj.addProperty("password", password.trim());

		JsonElement user = sendRequest(useContexts(context, "users/login"), bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST).getJson();

		refreshUserPermissions(user.getAsJsonObject().get("id").getAsLong(), context);

		return new Gson().fromJson(user, LSUser.class);
	}

	@Override
	public Boolean logout(Long userid, String context) throws LintException, TimeoutException {
		JsonElement json = sendRequest(useContexts(context, "users/" + userid + "/logout"), null, LintConf.CONTENT_TYPE, HttpMethod.GET).getJson();
		if(json.getAsJsonObject().get("success").getAsBoolean()){
			Cache.delete("p" + userid);
			Cache.delete("contexts");
			Cache.delete("usr_" + userid);
			Cache.delete(userid + ".profiles");
			return true;
		}
		return false;
	}

	@Override
	public LSUser createUser(String login, String email, String name, String context) throws LintException, TimeoutException {
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login);
		bodyObj.addProperty("email", email);
		bodyObj.addProperty("name", name);

		LSUser usr = new Gson().fromJson(sendRequest(useContexts(context, "users"), bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST).getJson(), LSUser.class);
		Cache.safeAdd("usr_" + usr.id, usr, LintConf.EXPIRATION);
		return usr;
	}

	@Override
	public LSUser createUser(String login, String email, String name, boolean ghost, String[] profiles, String context) throws LintException, TimeoutException {
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
		LSUser usr = new Gson().fromJson(sendRequest(useContexts(context, "users"), bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST).getJson(), LSUser.class);
		Cache.safeAdd("usr_" + usr.id, usr, LintConf.EXPIRATION);
		return usr;
	}

	@Override
	public LSUser registerUser(String password, String token, String context) throws LintException, TimeoutException {
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("token", token);
		bodyObj.addProperty("password", password);
		LSUser usr = new Gson().fromJson(sendRequest(useContexts(context, "users/register"), bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST).getJson(), LSUser.class);
		Cache.safeReplace("usr_" + usr.id, usr, LintConf.EXPIRATION);
		return usr;
	}

	@Override
	public LSUser activateUser(Long userid, String context) throws LintException, TimeoutException {
		LSUser usr = new Gson().fromJson(sendRequest(useContexts(context, "users/" + userid + "/activate"), null, LintConf.CONTENT_TYPE, HttpMethod.GET).getJson(), LSUser.class);
		Cache.safeReplace("usr_" + usr.id, usr, LintConf.EXPIRATION);
		return usr;
	}

	@Override
	public LSUser deactivateUser(Long userid, String context) throws LintException, TimeoutException {
		LSUser usr = new Gson().fromJson(sendRequest(useContexts(context, "users/" + userid + "/deactivate"), null, LintConf.CONTENT_TYPE, HttpMethod.GET).getJson(), LSUser.class);
		Cache.safeReplace("usr_" + usr.id, usr, LintConf.EXPIRATION);
		return usr;
	}

	@Override
	public LSUser reactivateUser(Long userid, String context) throws LintException, TimeoutException {
		return new Gson().fromJson(sendRequest(useContexts(context, "users/" + userid + "/reactivate"), null, LintConf.CONTENT_TYPE, HttpMethod.GET).getJson(), LSUser.class);
	}

	@Override
	public ArrayList<LSUser> getUsers(String context) throws LintException, TimeoutException {
		ArrayList<LSUser> list = null;
		if(contextPresent(context)){
			list = Cache.get(context + ".users", ArrayList.class);
		}else{
			list = Cache.get("users", ArrayList.class);
		}

		if(list == null){
			Type collectionType = new TypeToken<ArrayList<LSUser>>(){}.getType();
			list = new Gson().fromJson(sendRequest(useContexts(context, "users"), null, LintConf.CONTENT_TYPE, HttpMethod.GET).getJson(), collectionType);

			if(contextPresent(context)){
				Cache.safeAdd(context + ".users", list, LintConf.EXPIRATION);
			}else{
				Cache.safeAdd("users", list, LintConf.EXPIRATION);
			}
		}
		return list;
	}

	@Override
	public LSUser getUser(Long userid, String context) throws LintException, TimeoutException {
		LSUser usr = Cache.get("usr_" + userid, LSUser.class);
		if(usr == null){
			usr = new Gson().fromJson(sendRequest(useContexts(context, "users/" + userid), null, LintConf.CONTENT_TYPE, HttpMethod.GET).getJson(), LSUser.class);
			Cache.safeAdd("usr_" + usr.id, usr, LintConf.EXPIRATION);
		}
		return usr;
	}

	@Override
	public Boolean deleteUser(Long userid, String context) throws LintException, TimeoutException {
		JsonElement json = sendRequest(useContexts(context, "users/" + userid), null, LintConf.CONTENT_TYPE, HttpMethod.DELETE).getJson();
		if(json.getAsJsonObject().get("success").getAsBoolean()){
			Cache.delete("usr_" + userid);
			Cache.delete("users");
			Cache.delete(context + ".users");
			Cache.delete(userid + ".profiles");
			Cache.delete("p" + userid);
			return true;
		}
		return false;
	}

	@Override
	public LSUser passwordRecovery(String email, String context) throws LintException, TimeoutException {
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("email", email);
		return new Gson().fromJson(sendRequest(useContexts(context, "users/password_recovery"), bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST).getJson(), LSUser.class);
	}

	@Override
	public LSUser changePassword(Long userid, String currentPass, String newPass, String context) throws LintException, TimeoutException {
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("currentpass", currentPass);
		bodyObj.addProperty("newpass", newPass);
		return new Gson().fromJson(sendRequest(useContexts(context, "users/" + userid + "/changepass"), bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST).getJson(), LSUser.class);
	}

	@Override
	public LSUser updateUser(Long userid, String login, String email, String name, boolean ghost, String[] profiles, String context) throws LintException, TimeoutException {
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

		LSUser usr = new Gson().fromJson(sendRequest(useContexts(context, "users/" + userid), bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.PUT).getJson(), LSUser.class);
		Cache.safeDelete("usr_" + userid);
		Cache.safeAdd("usr_" + usr.id, usr, LintConf.EXPIRATION);
		return usr;
	}

	@Override
	public LSUser updateUser(Long userid, String login, String email, String name, String context) throws LintException, TimeoutException {
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("login", login);
		bodyObj.addProperty("email", email);
		bodyObj.addProperty("name", name);

		LSUser usr = new Gson().fromJson(sendRequest(useContexts(context, "users/" + userid), bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.PUT).getJson(), LSUser.class);
		Cache.safeDelete("usr_" + userid);
		Cache.safeAdd("usr_" + usr.id, usr, LintConf.EXPIRATION);
		return usr;
	}

	@Override
	public ArrayList<LSProfile> getProfiles(String context) throws LintException, TimeoutException {
		ArrayList<LSProfile> list = Cache.get(context + ".profiles", ArrayList.class);
		if(list == null){
			Type collectionType = new TypeToken<ArrayList<LSProfile>>(){}.getType();
			list = new Gson().fromJson(sendRequest(useContexts(context, "profiles"), null, LintConf.CONTENT_TYPE, HttpMethod.GET).getJson(), collectionType);
			Cache.safeAdd(context + ".profiles", list, LintConf.EXPIRATION);
		}
		return list;
	}

	@Override
	public ArrayList<LSProfile> getUserProfiles(Long userId, String context) throws LintException, TimeoutException {
		ArrayList<LSProfile> list = Cache.get(userId + ".profiles", ArrayList.class);
		if(list == null){
			Type collectionType = new TypeToken<ArrayList<LSProfile>>(){}.getType();
			list = new Gson().fromJson(sendRequest(useContexts(context, "users/" + userId + "/profiles"), null, LintConf.CONTENT_TYPE, HttpMethod.GET).getJson(), collectionType);
			Cache.safeAdd(userId + ".profiles", list, LintConf.EXPIRATION);
		}
		return list;
	}

	@Override
	public ArrayList<String> getUserPermissions(Long userid, String context) throws LintException, TimeoutException {
		ArrayList<String> list = Cache.get("p"+userid, ArrayList.class);
		if(list == null){
			refreshUserPermissions(userid, context);
		}
		return Cache.get("p"+userid, ArrayList.class);
	}

	@Override
	public ArrayList<LSContext> getContexts() throws LintException, TimeoutException {
		ArrayList<LSContext> list = Cache.get("contexts", ArrayList.class);
		if(list == null){
			Type collectionType = new TypeToken<ArrayList<LSContext>>(){}.getType();
			list = new Gson().fromJson(sendRequest("contexts", null, LintConf.CONTENT_TYPE, HttpMethod.GET).getJson(), collectionType);
			Cache.safeAdd("contexts", list, LintConf.EXPIRATION);
		}
		return list;
	}

	@Override
	public LSContext getDefaultContext() throws LintException, TimeoutException {
		ArrayList<LSContext> contexts = getContexts();
		for (LSContext lsContext : contexts) {
			if(lsContext.defaultContext){
				return lsContext;
			}
		}
		return null;
	}

	@Override
	public LSContext createContext(String name, String activationUrl, String url, String description, String copyContext) throws LintException, TimeoutException {
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("name", name);
		if (activationUrl != null) {
			bodyObj.addProperty("activationUrl", activationUrl);
		}
		if (url != null) {
			bodyObj.addProperty("url", url);
		}
		if (description != null) {
			bodyObj.addProperty("description", description);
		}

		if (copyContext != null) {
			bodyObj.addProperty("copycontext", copyContext);
		}

		LSContext ctx = new Gson().fromJson(sendRequest("contexts", bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST).getJson(), LSContext.class);
		Cache.safeAdd("ctx_" + name, ctx, LintConf.EXPIRATION);
		return ctx;
	}

	@Override
	public LSContext updateContext(String newName, String activationUrl, String url, String description, String oldName) throws LintException, TimeoutException {
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("name", newName);
		bodyObj.addProperty("activationUrl", activationUrl);
		bodyObj.addProperty("url", url);
		if (description != null) {
			bodyObj.addProperty("description", description);
		}
		LSContext ctx = new Gson().fromJson(sendRequest("contexts/" + oldName, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.PUT).getJson(), LSContext.class);
		Cache.safeDelete("ctx_" + oldName);
		Cache.safeAdd("ctx_" + newName, ctx, LintConf.EXPIRATION);
		return ctx;
	}

	@Override
	public LSContext getContext(String context) throws LintException, TimeoutException {
		LSContext ctx = Cache.get("ctx_" + context, LSContext.class);
		if(ctx == null){
			ctx = new Gson().fromJson(sendRequest("contexts/" + context, null, LintConf.CONTENT_TYPE, HttpMethod.GET).getJson(), LSContext.class);
			Cache.safeAdd("ctx_" + context, ctx, LintConf.EXPIRATION);
		}
		return ctx;
	}

	@Override
	public Boolean deleteContext(String context) throws LintException, TimeoutException {
		JsonElement json = sendRequest("contexts/" + context, null, LintConf.CONTENT_TYPE, HttpMethod.DELETE).getJson();
		if(json.getAsJsonObject().get("success").getAsBoolean()){
			Cache.delete("ctx_" + context);
			Cache.delete("contexts");
			Cache.delete(context + ".profiles");
			return true;
		}
		return false;
	}

	@Override
	public LSInstance getInstanceInfo() throws LintException, TimeoutException {
		LSInstance instance = Cache.get("application", LSInstance.class);
		if(instance == null){
			instance = new Gson().fromJson(sendRequest("", null, LintConf.CONTENT_TYPE, HttpMethod.GET).getJson(), LSInstance.class);
			Cache.safeAdd("application", instance, LintConf.EXPIRATION);
		}
		return instance;
	}

	/**
	 * refresh cached user permissions! This may take a few minutes
	 * 
	 * @param userid
	 * @param context
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public void refreshUserPermissions(Long userid, String context) throws LintException, TimeoutException{
		ArrayList<String> perms = userPermissions(userid, context);
		if(Cache.get("p" + userid) != null){
			Cache.safeReplace("p" + userid, perms, LintConf.EXPIRATION);
		}else{
			Cache.safeAdd("p" + userid, perms, LintConf.EXPIRATION);
		}
		Logger.debug(LintConf.LOG_PREFIX + "Permissions Loaded and cached for user " + userid + ".");
	}

	/**
	 * gets user permissions
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws LintException
	 * @throws TimeoutExceptionprofiles
	 */
	private ArrayList<String> userPermissions(Long userid, String context) throws LintException, TimeoutException{
		HttpResponse resp = sendRequest(useContexts(context, "users/" + userid + "/permissions"), null, LintConf.CONTENT_TYPE, HttpMethod.GET);

		Gson gson = new Gson();
		LSPermission[] permissions = gson.fromJson(resp.getJson(), LSPermission[].class);

		Logger.debug(LintConf.LOG_PREFIX + "Computes user permissions...");

		ArrayList<String> perms = new ArrayList<String>();
		for (LSPermission permission : permissions) {
			for (String action : permission.actions.keySet()) {
				for (String ap : permission.actions.get(action)) {
					perms.add(action + "." + ap);
				}
			}
		}
		return perms;
	}


	/**
	 * Check if a user has permission to access some Controller.Action
	 * 
	 * @param controlerAction
	 * @return true if OK otherwise false
	 * @throws LintException
	 * @throws TimeoutException
	 */
	private boolean checkActionAccess(String controlerAction, Long userid, String context) throws LintException, TimeoutException {
		ArrayList<String> perms = Cache.get("p" + userid, ArrayList.class);
		if(perms == null){
			refreshUserPermissions(userid, context);
			perms = Cache.get("p" + userid, ArrayList.class);
		}
		return perms.contains(controlerAction) ? true : false;
	}
}
