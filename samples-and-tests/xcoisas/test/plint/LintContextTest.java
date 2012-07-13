package plint;

import java.util.concurrent.TimeoutException;

import lagoon.LintRobot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

import com.google.gson.JsonElement;

import exceptions.LintException;

public class LintContextTest extends UnitTest {

	@Before
	public void setup() throws LintException, TimeoutException {
		//create subdomain for test
		LintRobot.createContext("test", "http://www.activationUrl.pt","http://www.url.pt", "test context", true);
	}

	@After
	public void clean() throws LintException, TimeoutException{
		// Delete context created for testing
		LintRobot.deleteContext("test");

	}

	@Test
	public void testListContexts(){
		try {
			JsonElement resp = LintRobot.getContexts();
			assertTrue(resp.isJsonArray());

		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}

	}

	@Test
	public void testCreateContext(){
		try {
			JsonElement resp = LintRobot.createContext("xtest2","http://www.activationUrl.pt", "http://www.url.pt", "xtest2 Description", true);
			assertTrue(resp.isJsonObject());
			LintRobot.deleteContext("xtest2");

		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testCreateContextNoActivationUrl(){
		try {
			JsonElement resp = LintRobot.createContext("xtest2", null, null, "xtest2 Description", true);
			assertTrue(resp.isJsonObject());
			LintRobot.deleteContext("xtest2");

		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testCreateContextNoDescription(){
		try {
			JsonElement resp = LintRobot.createContext("xtest2",null, null,  null, true);
			assertTrue(resp.isJsonObject());
			LintRobot.deleteContext("xtest2");

		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testUpdateContext(){
		try {
			JsonElement resp = LintRobot.updateContext("test", "http://www.updatetActivationUrl", "http://www.updatedUrl.com", "NewDomainDescription", "test");
			assertTrue(resp.getAsJsonObject().get("description").getAsString().equals("NewDomainDescription"));
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testUpdateContextNoDescription(){
		try {
			JsonElement resp = LintRobot.updateContext("test", "http://www.updatetActivationUrl", "http://www.updatedUrl.com", null, "test");
			assertTrue(resp.getAsJsonObject().get("name").getAsString().equals("test"));
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testUpdateUnexistentContext(){
		try {
			JsonElement resp = LintRobot.updateContext("xtest2", null, null,"NewDomainDescription", "xtest3");
			assertFalse(resp.isJsonObject());
		} catch (LintException e) {
			assertTrue(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(),true);
		}
	}

	@Test
	public void testGetContextInfo(){
		try {
			JsonElement resp = LintRobot.showContext("test");
			assertTrue(resp.getAsJsonObject().get("name").getAsString().equals("test"));
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testGetUnexistentContextInfo(){
		try {
			JsonElement resp = LintRobot.showContext("xtest2");
			assertFalse(resp.isJsonObject());
		} catch (LintException e) {
			assertTrue(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(),true);
		}
	}

	@Test
	public void testDeleteContext(){
		try {
			LintRobot.createContext("xtest2", null, null,"test2 context", true);
			JsonElement resp = LintRobot.deleteContext("xtest2");
			assertTrue(resp.getAsJsonObject().get("success").getAsString().equals("true"));
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testDeleteUnexistentContext(){
		try {
			JsonElement resp = LintRobot.deleteContext("xtest2");
			assertFalse(resp.isJsonObject());
		} catch (LintException e) {
			assertTrue(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(),true);
		}
	}
}
