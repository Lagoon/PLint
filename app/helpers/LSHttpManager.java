package helpers;

import java.util.concurrent.TimeoutException;

import org.jboss.netty.handler.codec.http.HttpMethod;

import play.Logger;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import utils.LintConf;

import com.google.gson.JsonObject;

import exceptions.LintException;

/**
 * Request helper
 * 
 * @author dpeixoto
 *
 */
public abstract class LSHttpManager {

	/**
	 * generic request maker using WS. All calls to remote Lagoon should use this method
	 * 
	 * @param partialUrl
	 * @param body
	 * @param contentType
	 * @return response
	 * @throws LintException
	 */
	protected HttpResponse sendRequest(String partialUrl, String body, String contentType, HttpMethod method) throws LintException, TimeoutException {
		WSRequest request = WS.url(LintConf.URL + partialUrl);
		request.headers.put("accept", "application/" + contentType);
		request.authenticate(LintConf.LOGIN, LintConf.PASSWORD);
		request.body(body);
		Logger.debug(LintConf.LOG_PREFIX + "Partial Request URL: " + partialUrl);
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

		Logger.debug(LintConf.LOG_PREFIX + "Request made to: " + request.url);
		return checkResponseCode(response);
	}

	/**
	 * computes context partial url
	 * 
	 * @param context
	 * @return
	 */
	protected String useContexts(String context, String partialUrl) {
		return contextPresent(context) ? partialUrl : "contexts/" + context + "/" + partialUrl;
	}

	/**
	 * checks if context is used
	 * 
	 * @param context
	 * @return
	 */
	protected boolean contextPresent(String context) {
		return (context == null || context.length() == 0);
	}

	/**
	 * verify if PLint can connect to remote Lagoon security successfully
	 * 
	 * @return true if OK otherwise falsePlintR
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public boolean checkConnection() throws LintException, TimeoutException {
		HttpResponse response = sendRequest("sanitycheck", null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		String message = response.getJson().getAsJsonObject().get("message").getAsString();
		if (response.getJson().getAsJsonObject().get("sanity").getAsBoolean()) {
			Logger.debug(LintConf.LOG_PREFIX + message);
			return true;
		} else {
			Logger.error(LintConf.LOG_PREFIX + "Your application is not working with Lagoon Security - " + message);
			return false;
		}
	}

	/**
	 * check if {@link HttpResponse} code is 2x
	 * 
	 * @param response
	 * @return
	 * @throws LintException
	 */
	private HttpResponse checkResponseCode(HttpResponse response) throws LintException {
		if (response == null || !response.success() || !response.getContentType().contains("json")) {
			JsonObject jsonObject = response.getJson().getAsJsonObject();
			Logger.debug(LintConf.LOG_PREFIX + "Response: " + jsonObject.toString());
			throw new LintException(jsonObject.get("error") + " :: " + jsonObject.get("message"));
		}
		return response;
	}
}
