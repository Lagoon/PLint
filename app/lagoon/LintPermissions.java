package lagoon;

import java.util.concurrent.TimeoutException;

import org.jboss.netty.handler.codec.http.HttpMethod;

import play.libs.WS.HttpResponse;
import utils.LintConf;

import com.google.gson.JsonElement;

import exceptions.LintException;

/**
 * Permission Management Methods
 * @author linda.velte
 *
 */
public class LintPermissions extends LintRobot{

	/**
	 * Gets list of users permissions
	 * @param idUserLagoon
	 * @param context
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public static JsonElement getPermissions(Long idUserLagoon, String context) throws LintException, TimeoutException{

		String partialUrl;
		if(context == null){
			partialUrl = "applications/" + identifier() + "/users/" + idUserLagoon + "/permissions";
		}else{
			partialUrl = "applications/" + identifier() + "/contexts/" + context + "/users/" + idUserLagoon + "/permissions";
		}

		HttpResponse resp = sendRequest(partialUrl, null, LintConf.CONTENT_TYPE, HttpMethod.GET);
		return resp.getJson();
	}

}
