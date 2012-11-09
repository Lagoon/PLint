package lagoon;

import java.util.concurrent.TimeoutException;

import org.jboss.netty.handler.codec.http.HttpMethod;

import play.libs.WS.HttpResponse;
import utils.LintConf;

import com.google.gson.JsonElement;

import exceptions.LintException;

/**
 * Permission Management Methods
 * 
 * @author linda.velte
 * 
 */
public class LintPermissions extends LintRobot {

	/**
	 * Gets list of users permissions
	 * 
	 * @param userid
	 * @param context
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getPermissions(Long userid, String context) throws LintException, TimeoutException {
		String partialUrl = checkContext(context) + "users/" + userid + "/permissions";;
		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

}
