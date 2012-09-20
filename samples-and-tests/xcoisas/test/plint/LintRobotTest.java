package plint;

import java.util.concurrent.TimeoutException;

import lagoon.LintRobot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import exceptions.LintException;

public class LintRobotTest extends UnitTest {

	@Before
	public void setup() throws LintException, TimeoutException {
		//create subdomain for test
		LintRobot.createContext("test",null, null,  "test context", null);
	}

	@After
	public void clean() throws LintException, TimeoutException{
		// Delete context created for testing
		LintRobot.deleteContext("test");
	}


	@Test
	public void testCheckConnection(){
		try {
			boolean resp = LintRobot.checkConnection();
			assertTrue(resp);
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
			e.printStackTrace();
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
			e.printStackTrace();
		}
	}
}
