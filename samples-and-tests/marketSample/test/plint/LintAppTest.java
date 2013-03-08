package plint;

import java.util.concurrent.TimeoutException;

import lagoon.PlintRobot;
import ls.LSInstance;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import exceptions.LintException;

public class LintAppTest extends UnitTest {

	@Before
	public void setup() throws LintException, TimeoutException {
		//create subdomain for test
		PlintRobot.getInstance().createContext("test", null,null,"test context", null);
	}

	@After
	public void clean() throws LintException, TimeoutException {
		// Delete context created for testing
		PlintRobot.getInstance().deleteContext("test");
	}

	@Test
	public void testGetAppInfo() {
		try {
			LSInstance instanceInfo = PlintRobot.getInstance().getInstanceInfo();
			assertNotNull(instanceInfo);
			assertEquals("Test", instanceInfo.name);
			assertEquals("Test Environment", instanceInfo.description);
			assertEquals("", instanceInfo.url);
			assertEquals("", instanceInfo.activationUrl);
			assertFalse(instanceInfo.notify);
			assertEquals("marketSample", instanceInfo.application.name);
			assertEquals("buy and sell your products", instanceInfo.application.description);
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}
}
