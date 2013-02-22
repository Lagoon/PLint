package lagoon.api;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import ls.LSContext;
import exceptions.LintException;

/**
 * Context resource operations
 * 
 * @author dpeixoto
 *
 */
public interface ContextResource {

	/**
	 * gets all contexts in environment.
	 * 
	 * @return {@link ArrayList}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public ArrayList<LSContext> getContexts() throws LintException, TimeoutException;

	/**
	 * creates new context in environment.
	 * 
	 * @param name
	 * @param activationUrl
	 * @param url
	 * @param description
	 * @param copyContext
	 * @return the new {@link LSContext}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSContext createContext(String name, String activationUrl, String url, String description, String copyContext) throws LintException, TimeoutException;

	/**
	 * updates an existing context in environment.
	 * 
	 * @param newName
	 * @param activationUrl
	 * @param url
	 * @param description
	 * @param oldName
	 * @return updated {@link LSContext}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSContext updateContext(String newName, String activationUrl, String url, String description, String oldName) throws LintException, TimeoutException;

	/**
	 * gets a context specified by name.
	 * 
	 * @param context
	 * @return {@link LSContext}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSContext getContext(String context) throws LintException, TimeoutException;

	/**
	 * gets the default context in environment.
	 * 
	 * @return {@link LSContext}
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public LSContext getDefaultContext() throws LintException, TimeoutException;

	/**
	 * deletes the context specified by name.
	 * 
	 * @param context
	 * @return {@code true} if ok {@code false} otherwise
	 * @throws LintException
	 * @throws TimeoutException
	 */
	public Boolean deleteContext(String context) throws LintException, TimeoutException;
}
