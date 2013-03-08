package plint;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import lagoon.PlintRobot;
import ls.LSProfile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import exceptions.LintException;

public class LintProfilesTest extends UnitTest {

	private final String CONTEXT_NAME = "test";

	@Before
	public void setup() throws LintException, TimeoutException {
		//create subdomain for test
		PlintRobot.getInstance().createContext(CONTEXT_NAME, null,null,"test context", null);
	}

	@After
	public void clean() throws LintException, TimeoutException {
		// Delete context created for testing
		PlintRobot.getInstance().deleteContext(CONTEXT_NAME);
	}

	@Test
	public void testGetProfiles() {
		try {
			ArrayList<LSProfile> profiles = PlintRobot.getInstance().getProfiles(CONTEXT_NAME);
			assertNotNull(profiles);
			assertEquals(4, profiles.size());
		} catch (LintException e) {
			assertFalse(e.getMessage(), true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(), true);
		}
	}
}