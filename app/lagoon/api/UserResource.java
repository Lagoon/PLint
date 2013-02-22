package lagoon.api;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import ls.LSUser;
import exceptions.LintException;

/**
 * User resource operations
 * 
 * @author dpeixoto
 *
 */
public interface UserResource{

	/**
	 * authenticates an user with specified {@code login} and {@code password} in this {@code context name}
	 * 
	 * @param login
	 * @param password
	 * @param context name
	 * @return {@link LSUser}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser login(String login, String password, String context) throws LintException, TimeoutException;

	/**
	 * makes logout of specified {@code userid}
	 * 
	 * @param userid
	 * @param context
	 * @return {@code true} if logout ok {@code false} otherwise
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public Boolean logout(Long userid, String context) throws LintException, TimeoutException;

	/**
	 * creates a user
	 * 
	 * @param login
	 * @param email
	 * @param name
	 * @param context
	 * @return created {@link LSUser}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser createUser(String login, String email, String name, String context) throws LintException, TimeoutException;

	/**
	 * creates a user
	 * 
	 * @param login
	 * @param email
	 * @param name
	 * @param ghost
	 * @param profiles names
	 * @param context name
	 * @return created {@link LSUser}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser createUser(String login, String email, String name, boolean ghost, String[] profiles, String context) throws LintException, TimeoutException;

	/**
	 * register a user after creation
	 * 
	 * @param desired password
	 * @param token
	 * @param context name
	 * @return regitered {@link LSUser}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser registerUser(String password, String token, String context) throws LintException, TimeoutException;

	/**
	 * activates an user
	 * 
	 * @param userid
	 * @param context name
	 * @return activated {@link LSUser}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser activateUser(Long userid, String context) throws LintException, TimeoutException;

	/**
	 * deactivates an user
	 * 
	 * @param userid
	 * @param context name
	 * @return deactivated {@link LSUser}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser deactivateUser(Long userid, String context) throws LintException, TimeoutException;

	/**
	 * reactivates an user
	 * 
	 * @param userid
	 * @param context name
	 * @return deactivated {@link LSUser}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser reactivateUser(Long userid, String context) throws LintException, TimeoutException;

	/**
	 * gets all enabled users from specified context
	 * 
	 * @param context name
	 * @return {@link ArrayList} of {@link LSUser}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public ArrayList<LSUser> getUsers(String context) throws LintException, TimeoutException;

	/**
	 * gets all info about specified {@code userid}
	 * 
	 * @param userid
	 * @param context name
	 * @return {@link LSUser}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser getUser(Long userid, String context) throws LintException, TimeoutException;

	/**
	 * deletes <b>permanently</b> the specified user
	 * 
	 * @param userid
	 * @param context name
	 * @return {@code true} if success operation {@code false} otherwise
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public Boolean deleteUser(Long userid, String context) throws LintException, TimeoutException;

	/**
	 * request password recovery for specified email
	 * 
	 * @param email
	 * @param context name
	 * @return {@link LSUser}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser passwordRecovery(String email, String context) throws LintException, TimeoutException;

	/**
	 * changes the password of the specified user
	 * 
	 * @param userid
	 * @param currentPass
	 * @param newPass
	 * @param context name
	 * @return {@link LSUser}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser changePassword(Long userid, String currentPass, String newPass, String context) throws LintException, TimeoutException;

	/**
	 * updates user attributes
	 * 
	 * @param userid
	 * @param login
	 * @param email
	 * @param name
	 * @param ghost
	 * @param profiles list names
	 * @param context name
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser updateUser(Long userid, String login, String email, String name, boolean ghost, String[] profiles, String context) throws LintException, TimeoutException;

	/**
	 * updates user attributes
	 * 
	 * @param userid
	 * @param login
	 * @param email
	 * @param name
	 * @param context
	 * @return
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSUser updateUser(Long userid, String login, String email, String name, String context) throws LintException, TimeoutException;

}
