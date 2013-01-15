package plint;

import java.util.concurrent.TimeoutException;

import lagoon.LintRobot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

import com.google.gson.JsonElement;

import exceptions.LintException;

public class LintUserTest extends UnitTest {

	private final String context = "test";

	@Before
	public void setup() throws LintException, TimeoutException {
		// create subdomain for context
		LintRobot.createContext(context, "http://xpto.lvh.me:9000/activations", null, "context context", null);
	}

	@After
	public void clean() throws LintException, TimeoutException {
		// Delete context created for testing
		LintRobot.deleteContext(context);
	}

	@Test
	public void testLogin() {
		try {
			JsonElement resp = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", context);
			String token = resp.getAsJsonObject().get("token").getAsString();
			LintRobot.registerUser("ola123", token, context);
			LintRobot.login("user1", "ola123", context);
			assertTrue(resp.getAsJsonObject().get("login").getAsString().equals("user1"));
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testLoginWrongUserAndPass() {
		try {
			Object resp = LintRobot.login("user", "password", context);
			assertFalse(resp.toString().equals("true"));
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testLogout() {
		try {
			JsonElement resp = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", context);
			String token = resp.getAsJsonObject().get("token").getAsString();
			JsonElement resp2 = LintRobot.registerUser("ola123", token, context);
			Long id = resp2.getAsJsonObject().get("id").getAsLong();
			boolean respLogout = LintRobot.logout(id, context);
			assertTrue(respLogout);
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testLogoutWrongUser() {
		try {
			Object resp = LintRobot.logout(1l, context);
			assertFalse(resp.toString(), true);
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testCreateUserNoProfiles() {
		try {
			JsonElement resp = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", false, null, context);
			assertTrue(resp.getAsJsonObject().get("login").getAsString().equals("user1"));
			assertTrue(resp.getAsJsonObject().get("name").getAsString().equals("user1"));

		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
			e.printStackTrace();
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
			e.printStackTrace();
		}
	}

	@Test
	public void testCreateExistentUser() {
		try {
			LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", "user1");
			JsonElement user2 = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", context);
			assertFalse(user2.isJsonObject());
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}

	}

	@Test
	public void testRegisterUser() {
		try {
			JsonElement resp = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", context);
			String token = resp.getAsJsonObject().get("token").getAsString();
			JsonElement respReg = LintRobot.registerUser("ola123", token, context);
			assertTrue(respReg.getAsJsonObject().get("name").getAsString().equals("user1"));
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}

	}

	@Test
	public void testRegisterInvalidToken() {
		try {
			LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", context);
			JsonElement resp = LintRobot.registerUser("ola123", "thisisaninvalidtoken", context);
			assertFalse(resp.isJsonObject());
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}

	}

	@Test
	public void testActivateUser() {

		String[] profiles = new String[1];
		profiles[0] = "guest";
		JsonElement user;
		try {
			user = LintRobot.createUser("user9", "xisgest@xlm.pt", "user1", false, profiles, context);
			Long id = user.getAsJsonObject().get("id").getAsLong();
			String token = user.getAsJsonObject().get("token").getAsString();
			LintRobot.registerUser("ola123", token, context);
			LintRobot.deactivateUser(id, context);
			JsonElement resp = LintRobot.activateUser(id, context);
			assertTrue(resp.getAsJsonObject().get("name").getAsString().equals("user1"));
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testActivateActiveUser() {

		String[] profiles = new String[1];
		profiles[0] = "admin";
		JsonElement user;
		try {
			user = LintRobot.createUser("user9", "xisgest@xlm.pt", "user1", false, profiles, context);
			Long id = user.getAsJsonObject().get("id").getAsLong();
			String token = user.getAsJsonObject().get("token").getAsString();
			LintRobot.registerUser("ola123", token, context);
			JsonElement resp = LintRobot.activateUser(id, context);
			assertFalse(resp.isJsonObject());

		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testDeactivateUser() {
		try {
			JsonElement user = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", context);
			Long id = user.getAsJsonObject().get("id").getAsLong();
			String token = user.getAsJsonObject().get("token").getAsString();
			LintRobot.registerUser("ola123", token, context);
			JsonElement resp = LintRobot.deactivateUser(id, context);
			assertTrue(resp.getAsJsonObject().get("name").getAsString().equals("user1"));
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testDeactivateNonRegistedUser() {
		try {
			JsonElement user = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", context);
			Long id = user.getAsJsonObject().get("id").getAsLong();
			JsonElement resp = LintRobot.deactivateUser(id, context);
			assertFalse(resp.isJsonObject());
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testReactivateUser() {
		try {
			JsonElement user = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", context);
			Long id = user.getAsJsonObject().get("id").getAsLong();
			JsonElement resp = LintRobot.reactivateUser(id, context);
			assertTrue(resp.getAsJsonObject().get("name").getAsString().equals("user1"));
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testListUsers() {
		try {
			JsonElement resp = LintRobot.getUsers(context);
			assertTrue(resp.isJsonArray());
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testGetUser() {
		JsonElement user;
		try {
			user = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", context);
			Long id = user.getAsJsonObject().get("id").getAsLong();
			JsonElement resp = LintRobot.showUser(id, context);
			assertTrue(resp.getAsJsonObject().get("login").getAsString().equals("user1"));
			assertTrue(resp.getAsJsonObject().get("name").getAsString().equals("user1"));
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testListUnexistentUser() {
		try {
			JsonElement resp = LintRobot.showUser(1l, context);
			assertFalse(resp.isJsonObject());
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testUpdateUserDefault() {
		try {
			JsonElement user = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", context);
			Long id = user.getAsJsonObject().get("id").getAsLong();
			JsonElement resp = LintRobot.updateUser(id, "user2", "xisgest@xlm.pt", "user1", context);
			assertTrue(resp.getAsJsonObject().get("login").getAsString().equals("user2"));
			assertTrue(resp.getAsJsonObject().get("email").getAsString().equals("xisgest@xlm.pt"));
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testUpdateUser() {
		String[] profiles = { "guest" };

		try {
			JsonElement user = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", false, profiles, context);
			Long id = user.getAsJsonObject().get("id").getAsLong();
			JsonElement resp = LintRobot.updateUser(id, "user2", "xisgest@xlm.pt", "user1", false, profiles, context);
			assertTrue(resp.getAsJsonObject().get("login").getAsString().equals("user2"));
			assertTrue(resp.getAsJsonObject().get("email").getAsString().equals("xisgest@xlm.pt"));
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testUpdateUserNoProfiles() {
		try {
			JsonElement user = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", false, null, context);
			Long id = user.getAsJsonObject().get("id").getAsLong();
			JsonElement resp = LintRobot.updateUser(id, "user2", "xisgest@xlm.pt", "user1", false, null, context);
			assertTrue(resp.getAsJsonObject().get("login").getAsString().equals("user2"));
			assertTrue(resp.getAsJsonObject().get("email").getAsString().equals("xisgest@xlm.pt"));
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testUpdateUnexistentUser() {
		try {
			JsonElement resp = LintRobot.updateUser(1l, "user2", "xisgest@xlm.pt", "user1", context);
			assertFalse(resp.isJsonObject());
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}
}
