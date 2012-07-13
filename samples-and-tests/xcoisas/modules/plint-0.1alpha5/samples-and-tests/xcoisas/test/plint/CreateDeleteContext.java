package plint;

import java.util.concurrent.TimeoutException;

import lagoon.LintRobot;

import org.junit.Test;

import com.google.gson.JsonElement;

import exceptions.LintException;

import play.test.UnitTest;

public class CreateDeleteContext extends UnitTest {
	
	@Test
	public void test() throws LintException, TimeoutException {
	
//		LintRobot.deleteContext("test");
		
		for(int i = 0; i < 100; i++) {
			
			System.out.println("TEST: " + i);
			
			LintRobot.createContext("test", "http://www.activationUrl.pt","http://www.url.pt", "test context", true);
			JsonElement resp = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", "test");
			String token = resp.getAsJsonObject().get("token").getAsString();
			LintRobot.registerUser("ola123", token, "test");
			LintRobot.login("user1", "ola123", "test");
			LintRobot.deleteContext("test");
			
			
			
			
//				try {
//					JsonElement resp = LintRobot.createUser("user1", "xisgest@xlm.pt", "user1", "test");
//					String token = resp.getAsJsonObject().get("token").getAsString();
//					LintRobot.registerUser("ola123", token, "test");
//					LintRobot.login("user1", "ola123", "test");
//					assertTrue(resp.getAsJsonObject().get("login").getAsString().equals("user1"));
//				} catch (LintException e) {
//					assertFalse(e.getMessage(),true);
//				} catch (TimeoutException e) {
//					assertFalse(e.getMessage(),true);
//				}
				
		}
	}
}
