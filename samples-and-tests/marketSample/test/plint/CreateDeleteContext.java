package plint;

import java.util.concurrent.TimeoutException;

import lagoon.LintRobot;

import org.junit.Test;

import play.test.UnitTest;

import com.google.gson.JsonElement;

import exceptions.LintException;

public class CreateDeleteContext extends UnitTest {

	@Test
	public void test() throws LintException, TimeoutException {

		//LintRobot.deleteContext("test");

		for (int i = 0; i < 1; i++) {

			LintRobot.createContext("test", "http://www.activationUrl.pt", "http://www.url.pt", "test context", null);
			JsonElement resp = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", "test");
			String token = resp.getAsJsonObject().get("token").getAsString();
			LintRobot.registerUser("ola123", token, "test");
			LintRobot.login("user1", "ola123", "test");
			LintRobot.deleteContext("test");
		}
	}
}
