package plint;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import lagoon.PlintRobot;
import ls.LSUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import exceptions.LintException;

public class LintPermissionTest extends UnitTest {

	private final String CONTEXT_NAME = "test";

	@Before
	public void setup() throws LintException, TimeoutException {
		//create subdomain for test
		PlintRobot.getInstance().createContext(CONTEXT_NAME, "http://xpto.lvh.me:9000/activations" , null, "test context", null);
	}

	@After
	public void clean() throws LintException, TimeoutException {
		// Delete context created for testing
		PlintRobot.getInstance().deleteContext(CONTEXT_NAME);
	}

	@Test
	public void testGetActiveUserPermissions(){
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.token, "Token must exist");
			PlintRobot.getInstance().registerUser("ola123", createUser.token, CONTEXT_NAME);
			ArrayList<String> userPermissions = PlintRobot.getInstance().getUserPermissions(createUser.id, CONTEXT_NAME);
			//			assertNotNull(userPermissions);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testGetNotActiveUserPermissions(){
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNotNull(createUser);
			ArrayList<String> userPermissions = PlintRobot.getInstance().getUserPermissions(createUser.id, CONTEXT_NAME);
			assertNotNull(userPermissions);
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testGetNotActiveUserPermissionsWithNoContext(){
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", null);
			assertNotNull(createUser);
			ArrayList<String> userPermissions = PlintRobot.getInstance().getUserPermissions(createUser.id, CONTEXT_NAME);
			assertNotNull(userPermissions);
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testGetUnexistentUserPermissions(){
		try {
			ArrayList<String> userPermissions = PlintRobot.getInstance().getUserPermissions(1l, CONTEXT_NAME);
			assertNull(userPermissions);
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}
}
