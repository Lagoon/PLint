package lagoon;

import java.util.concurrent.TimeoutException;

import org.jboss.netty.handler.codec.http.HttpMethod;

import play.libs.WS.HttpResponse;
import utils.LintConf;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import exceptions.LintException;

/**
 * Context Management Methods
 * 
 * @author linda.velte
 * 
 */
public class LintContext extends LintRobot {

	/**
	 * List current Contexts on application
	 * 
	 * @return json with existing Contexts info
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getContexts() throws LintException, TimeoutException {
		String partialUrl = "contexts";
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * Create Contexts on application
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
		String partialUrl = "contexts";

		// Build Body
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

		// send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST);
		return resp.getJson();
	}

	/**
	 * Update context(Context)
	 * 
	 * @param newName
	 *            - new Context
	 * @param description
	 *            - new Context description
	 * @param oldName
	 *            - current Context (Context to be updated)
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement updateContext(String newName, String activationUrl, String url, String description, String oldName) throws LintException, TimeoutException {
		String partialUrl = "contexts/" + oldName;

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("name", newName);
		bodyObj.addProperty("activationUrl", activationUrl);
		bodyObj.addProperty("url", url);
		if (description != null) {
			bodyObj.addProperty("description", description);
		}

		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.PUT);
		return resp.getJson();
	}

	/**
	 * Get context information
	 * 
	 * @param context
	 * @return jsonElement with Context information
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement showContext(String context) throws LintException, TimeoutException {
		String partialUrl = "contexts/" + context;
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * Delete context
	 * 
	 * @param context
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement deleteContext(String context) throws LintException, TimeoutException {
		String partialUrl = "contexts/" + context;
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.DELETE);
		return resp.getJson();
	}
}
