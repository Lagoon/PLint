package plint;

import java.util.concurrent.TimeoutException;

import lagoon.PlintRobot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import exceptions.LintException;

public class LintRobotTest extends UnitTest {

	private final String CONTEXT_NAME = "test";

	@Before
	public void setup() throws LintException, TimeoutException {
		//create subdomain for test
		PlintRobot.getInstance().createContext(CONTEXT_NAME, null , null, "test context", null);
	}

	@After
	public void clean() throws LintException, TimeoutException {
		// Delete context created for testing
		PlintRobot.getInstance().deleteContext(CONTEXT_NAME);
	}


	@Test
	public void testCheckConnection(){
		try {
			assertTrue(PlintRobot.getInstance().checkConnection());
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}
}
