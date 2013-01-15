package plint;

import java.util.concurrent.TimeoutException;

import lagoon.LintRobot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

import com.google.gson.JsonElement;

import exceptions.LintException;

public class LintPermissionTest extends UnitTest {
	@Before
	public void setup() throws LintException, TimeoutException {
		//create subdomain for test
		LintRobot.createContext("test", "http://xpto.lvh.me:9000/activations" , null, "test context", null);
	}

	@After
	public void clean() throws LintException, TimeoutException{
		// Delete context created for testing
		LintRobot.deleteContext("test");
	}

	@Test
	public void testGetActiveUserPermissions(){
		try {
			JsonElement user = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", "test");
			Long id = user.getAsJsonObject().get("id").getAsLong();
			String token = user.getAsJsonObject().get("token").getAsString();
			LintRobot.registerUser("ola123", token, "test");
			JsonElement resp = LintRobot.getPermissions(id,"test");
			assertTrue(resp.isJsonArray());
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testGetNotActiveUserPermissions(){
		try {
			JsonElement user = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", "test");
			Long id = user.getAsJsonObject().get("id").getAsLong();
			JsonElement resp = LintRobot.getPermissions(id,"test");
			assertFalse(resp.isJsonArray());
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testGetNotActiveUserPermissionsWithNoContext(){
		try {
			JsonElement user = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", null);
			Long id = user.getAsJsonObject().get("id").getAsLong();
			JsonElement resp = LintRobot.getPermissions(id,"test");
			assertFalse(resp.isJsonArray());
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}

	@Test
	public void testGetUnexistentUserPermissions(){
		try {

			JsonElement resp = LintRobot.getPermissions(1l,"test");
			assertFalse(resp.isJsonArray());
		} catch (LintException e) {
			assertTrue(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(), true);
		}
	}
}
