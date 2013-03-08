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

public class LintUserTest extends UnitTest {

	private final String CONTEXT_NAME = "test";


	@Before
	public void setup() throws LintException, TimeoutException {
		//create subdomain for test
		PlintRobot.getInstance().createContext(CONTEXT_NAME, "http://xpto.lvh.me:9000/activations", null, "context context", null);
	}

	@After
	public void clean() throws LintException, TimeoutException {
		// Delete context created for testing
		PlintRobot.getInstance().deleteContext(CONTEXT_NAME);
	}


	@Test
	public void testLogin() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.token);
			PlintRobot.getInstance().registerUser("ola123", createUser.token, CONTEXT_NAME);
			LSUser login = PlintRobot.getInstance().login("user1", "ola123", CONTEXT_NAME);
			assertNotNull(login);
			assertEquals("user1", login.name);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testLoginWrongUserAndPass() {
		try {
			LSUser login = PlintRobot.getInstance().login("user", "password", CONTEXT_NAME);
			assertNull(login);
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testLogout() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.token);

			PlintRobot.getInstance().registerUser("ola123", createUser.token, CONTEXT_NAME);
			LSUser login = PlintRobot.getInstance().login("user1", "ola123", CONTEXT_NAME);
			assertNotNull(login);
			assertEquals("user1", login.name);
			assertTrue(PlintRobot.getInstance().logout(login.id, CONTEXT_NAME));
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testLogoutWrongUser() {
		try {
			assertFalse(PlintRobot.getInstance().logout(1l, CONTEXT_NAME));
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testCreateUserNoProfiles() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", false, null, CONTEXT_NAME);
			assertEquals("user1", createUser.name);
			assertEquals("user1", createUser.login);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testCreateExistentUser() {
		try {
			LSUser createUser1 = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNotNull(createUser1);
			LSUser createUser2 = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNull(createUser2);
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}

	}

	@Test
	public void testRegisterUser() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.token);
			LSUser registerUser = PlintRobot.getInstance().registerUser("ola123", createUser.token, CONTEXT_NAME);
			assertEquals("user1", createUser.name);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}

	}

	@Test
	public void testRegisterInvalidToken() {
		try {
			PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			LSUser registerUser = PlintRobot.getInstance().registerUser("ola123", "thisisaninvalidtoken", CONTEXT_NAME);
			assertNull(registerUser);
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}

	}

	//	@Test
	//	public void testActivateUser() {
	//		try {
	//			LSUser createUser = PlintRobot.getInstance().createUser("user9", "xisgest@xlm.pt", "user1", false, new String[]{"guest"}, CONTEXT_NAME);
	//			assertNotNull(createUser);
	//			assertNotNull(createUser.token);
	//			LSUser registerUser = PlintRobot.getInstance().registerUser("ola123", createUser.token, CONTEXT_NAME);
	//			LSUser activateUser = PlintRobot.getInstance().activateUser(createUser.id, CONTEXT_NAME);
	//			assertNotNull(activateUser);
	//			assertEquals("user1", activateUser.login);
	//		} catch (LintException e) {
	//			assertFalse(e.getMessage(), true);
	//		} catch (TimeoutException e) {
	//			assertFalse(e.getMessage(), true);
	//		}
	//	}

	@Test
	public void testActivateActiveUser() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user9", "xisgest@xlm.pt", "user1", false, new String[]{"admin"}, CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.id);
			assertNotNull(createUser.token);
			PlintRobot.getInstance().registerUser("ola123", createUser.token, CONTEXT_NAME);
			LSUser activateUser = PlintRobot.getInstance().activateUser(createUser.id, CONTEXT_NAME);
			assertNull(activateUser);
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testDeactivateUser() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.id);
			assertNotNull(createUser.token);
			LSUser registerUser = PlintRobot.getInstance().registerUser("ola123", createUser.token, CONTEXT_NAME);
			LSUser deactivateUser = PlintRobot.getInstance().deactivateUser(registerUser.id, CONTEXT_NAME);
			assertNotNull(deactivateUser);
			assertEquals("user1", deactivateUser.name);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testDeactivateNonRegistedUser() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.id);
			LSUser deactivateUser = PlintRobot.getInstance().deactivateUser(createUser.id, CONTEXT_NAME);
			assertNotNull(deactivateUser);
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testReactivateUser() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.id);
			LSUser reactivateUser = PlintRobot.getInstance().reactivateUser(createUser.id, CONTEXT_NAME);
			assertNotNull(reactivateUser);
			assertEquals("user1", reactivateUser.name);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testListUsers() {
		try {
			ArrayList<LSUser> users = PlintRobot.getInstance().getUsers(CONTEXT_NAME);
			assertNotNull(users);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testGetUser() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.id);
			LSUser user = PlintRobot.getInstance().getUser(createUser.id, CONTEXT_NAME);
			assertNotNull(user);
			assertEquals("user1", user.login);
			assertEquals("user1", user.name);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testListUnexistentUser() {
		try {
			LSUser user = PlintRobot.getInstance().getUser(1l, CONTEXT_NAME);
			assertNull(user);
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testUpdateUserDefault() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.id);

			LSUser updateUser = PlintRobot.getInstance().updateUser(createUser.id, "user2", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertEquals("user2", updateUser.login);
			assertEquals("xisgest@xlm.pt", updateUser.email);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testUpdateUser() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", false, new String[]{"guest"}, CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.id);
			LSUser updateUser = PlintRobot.getInstance().updateUser(createUser.id, "user2", "xisgest@xlm.pt", "user1", false, new String[]{"guest"}, CONTEXT_NAME);
			assertEquals("user2", updateUser.login);
			assertEquals("xisgest@xlm.pt", updateUser.email);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testUpdateUserNoProfiles() {
		try {
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", false, null, CONTEXT_NAME);
			assertNotNull(createUser);
			assertNotNull(createUser.id);
			LSUser updateUser = PlintRobot.getInstance().updateUser(createUser.id, "user2", "xisgest@xlm.pt", "user1", false, null, CONTEXT_NAME);
			assertEquals("user2", updateUser.login);
			assertEquals("xisgest@xlm.pt", updateUser.email);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testUpdateUnexistentUser() {
		try {
			LSUser updateUser = PlintRobot.getInstance().updateUser(1l, "user2", "xisgest@xlm.pt", "user1", CONTEXT_NAME);
			assertNull(updateUser);
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}
}
