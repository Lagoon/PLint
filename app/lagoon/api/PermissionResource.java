package lagoon.api;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import ls.LSUser;
import exceptions.LintException;

/**
 * Permission resource operations
 * 
 * @author dpeixoto
 *
 */
public interface PermissionResource {

	/**
	 * gets {@link LSUser} permissions in format: action.actionPoint
	 * 
	 * @param userid
	 * @param context
	 * @return {@link ArrayList} with all user permissions
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public ArrayList<String> getUserPermissions(Long userid, String context) throws LintException, TimeoutException;
}
