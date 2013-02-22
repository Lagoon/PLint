package lagoon.api;

import java.util.concurrent.TimeoutException;

import ls.LSInstance;
import exceptions.LintException;

/**
 * Instance resource operations
 * 
 * @author dpeixoto
 *
 */
public interface InstanceResource {

	/**
	 * gets all Application info
	 * @return {@link LSInstance}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSInstance getInstanceInfo() throws LintException, TimeoutException;
}
