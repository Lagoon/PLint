package lagoon.api;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import ls.LSProfile;
import exceptions.LintException;

/**
 * Profile resource operations
 * 
 * @author dpeixoto
 *
 */
public interface ProfileResource{

	/**
	 * gets all {@link LSProfile} in context specified by name.
	 * 
	 * @param context
	 * @return {@link ArrayList}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public ArrayList<LSProfile> getProfiles(String context) throws LintException, TimeoutException;

	/**
	 * gets all {@link LSProfile} that specified {@code userId} in {@code context} has.
	 * 
	 * @param userId
	 * @param context
	 * @return {@link ArrayList}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public ArrayList<LSProfile> getUserProfiles(Long userId, String context) throws LintException, TimeoutException;
}
