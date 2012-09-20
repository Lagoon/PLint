package lagoon;

import java.util.concurrent.TimeoutException;

import org.jboss.netty.handler.codec.http.HttpMethod;

import play.libs.WS.HttpResponse;
import utils.LintConf;

import com.google.gson.JsonElement;

import exceptions.LintException;

/**
 * Profiles Management Methods
 * 
 * @author linda.velte
 * 
 */
public class LintProfiles extends LintRobot {

	/**
	 * Returns all active profiles
	 * 
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getProfiles(String context) throws LintException, TimeoutException {

		String partialUrl;
		if (context == null) {
			partialUrl = "profiles";
		} else {
			partialUrl = "contexts/" + context + "/profiles";
		}

		// send request
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}
}
