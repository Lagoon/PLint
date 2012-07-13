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
 * @author linda.velte
 *
 */
public class LintContext extends LintRobot{

	/**
	 * List current Contexts on application
	 * @return json with existing Contexts info
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getContexts() throws LintException, TimeoutException{
		String partialUrl = "applications/" + identifier() + "/contexts";
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * Create Contexts on application
	 * @param name
	 * @param activationUrl
	 * @param url
	 * @param description
	 * @param copyContext
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement createContext(String name, String activationUrl, String url, String description, boolean copyContext) throws LintException, TimeoutException{
		String partialUrl = "applications/" + identifier() + "/contexts";

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("name", name);
		if(activationUrl != null){
			bodyObj.addProperty("activationUrl", activationUrl);
		}
		if(url != null){
			bodyObj.addProperty("url", url);
		}
		if(description != null){
			bodyObj.addProperty("description", description);
		}

		bodyObj.addProperty("copycontext", copyContext);

		//send request
		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.POST);
		return resp.getJson();
	}

	/**
	 * Update context(Context)
	 * @param newName - new Context
	 * @param description - new Context description
	 * @param oldName - current Context (Context to be updated)
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement updateContext(String newName, String activationUrl, String url, String description, String oldName) throws LintException, TimeoutException{
		String partialUrl = "applications/" + identifier() + "/contexts/" + oldName;

		// Build Body
		JsonObject bodyObj = new JsonObject();
		bodyObj.addProperty("name", newName);
		bodyObj.addProperty("activationUrl", activationUrl);
		bodyObj.addProperty("url", url);
		if(description!=null){
			bodyObj.addProperty("description", description);
		}

		HttpResponse resp = sendRequest(partialUrl, bodyObj.toString(), LintConf.CONTENT_TYPE, HttpMethod.PUT);
		return resp.getJson();
	}

	/**
	 * Get context information
	 * @param contextName - Context
	 * @return jsonElement with Context information
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement showContext(String contextName) throws LintException, TimeoutException{
		String partialUrl = "applications/" + identifier() + "/contexts/" + contextName;
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

	/**
	 * Delete context
	 * @param contextName - Context to be deleted
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement deleteContext(String contextName) throws LintException, TimeoutException{
		String partialUrl = "applications/" + identifier() + "/contexts/" + contextName;
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.DELETE);
		return resp.getJson();
	}
}
