package plint;

import java.util.concurrent.TimeoutException;

import lagoon.LintRobot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

import com.google.gson.JsonElement;

import exceptions.LintException;

public class LintProfilesTest extends UnitTest {

	@Before
	public void setup() throws LintException, TimeoutException {
		//create subdomain for test
		LintRobot.createContext("test",null, null,  "test context", true);
	}

	@After
	public void clean() throws LintException, TimeoutException {
		// Delete context created for testing
		LintRobot.deleteContext("test");
	}


	@Test
	public void testGetProfiles() {
		try {
			JsonElement resp = LintRobot.getProfiles("test");
			assertTrue(resp.isJsonArray());
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testGetProfilesWithNoContext() {
		try {
			JsonElement resp = LintRobot.getProfiles(null);
			assertTrue(resp.isJsonArray());
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}
}
