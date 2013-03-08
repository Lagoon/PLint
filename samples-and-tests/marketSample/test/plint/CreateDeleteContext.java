package plint;

import java.util.concurrent.TimeoutException;

import lagoon.PlintRobot;
import ls.LSUser;

import org.junit.Test;

import play.test.UnitTest;
import exceptions.LintException;

public class CreateDeleteContext extends UnitTest {

	@Test
	public void test() throws LintException, TimeoutException {

		for (int i = 0; i < 2; i++) {
			PlintRobot.getInstance().createContext("test", "http://www.activationUrl.pt", "http://www.url.pt", "test context", null);
			LSUser createUser = PlintRobot.getInstance().createUser("user1", "xisgest@xlm.pt", "user1", "test");
			assertNotNull(createUser);
			assertNotNull(createUser.token);
			PlintRobot.getInstance().registerUser("ola123", createUser.token, "test");
			PlintRobot.getInstance().login("user1", "ola123", "test");
			PlintRobot.getInstance().deleteContext("test");
		}
	}
}
