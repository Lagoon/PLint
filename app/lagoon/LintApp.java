package lagoon;

import java.util.concurrent.TimeoutException;

import org.jboss.netty.handler.codec.http.HttpMethod;

import play.libs.WS.HttpResponse;
import utils.LintConf;

import com.google.gson.JsonElement;

import exceptions.LintException;

/**
 * Application Management methods
 * 
 * @author linda.velte
 * 
 */
public class LintApp extends LintRobot {

	/**
	 * Get application info including modules
	 * 
	 * @return jsonelement containing all information
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement showApp() throws LintException, TimeoutException {

		// send request
		HttpResponse resp = sendRequest("", null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}
}