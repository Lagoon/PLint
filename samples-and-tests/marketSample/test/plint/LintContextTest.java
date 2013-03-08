package plint;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import lagoon.PlintRobot;
import ls.LSContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;
import exceptions.LintException;

public class LintContextTest extends UnitTest {

	private final String CONTEXT_NAME = "test";

	@Before
	public void setup() throws LintException, TimeoutException {
		//create subdomain for test
		PlintRobot.getInstance().createContext(CONTEXT_NAME, "http://www.activationUrl.pt", "http://www.url.pt", "test context", null);
	}

	@After
	public void clean() throws LintException, TimeoutException {
		// Delete context created for testing
		PlintRobot.getInstance().deleteContext(CONTEXT_NAME);
	}


	@Test
	public void testListContexts(){
		try {
			ArrayList<LSContext> contexts = PlintRobot.getInstance().getContexts();
			assertNotNull(contexts);
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}

	}

	@Test
	public void testCreateContext(){
		try {
			LSContext createContext = PlintRobot.getInstance().createContext("xtest2","http://www.activationUrl.pt", "http://www.url.pt", "xtest2 Description", null);
			assertNotNull(createContext);
			assertEquals("xtest2", createContext.name);
			PlintRobot.getInstance().deleteContext("xtest2");
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testCreateContextNoActivationUrl(){
		try {
			LSContext createContext = PlintRobot.getInstance().createContext("xtest2", null, null, "xtest2 Description", null);
			assertNotNull(createContext);
			PlintRobot.getInstance().deleteContext("xtest2");
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testCreateContextNoDescription(){
		try {
			LSContext createContext = PlintRobot.getInstance().createContext("xtest2", null, null, null, null);
			assertNotNull(createContext);
			PlintRobot.getInstance().deleteContext("xtest2");
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testUpdateContext(){
		try {
			LSContext updateContext = PlintRobot.getInstance().updateContext("test", "http://www.updatetActivationUrl", "http://www.updatedUrl.com", "NewDomainDescription", CONTEXT_NAME);
			assertNotNull(updateContext);
			assertEquals("NewDomainDescription", updateContext.description);
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testUpdateContextNoDescription(){
		try {
			LSContext updateContext = PlintRobot.getInstance().updateContext("test", "http://www.updatetActivationUrl", "http://www.updatedUrl.com", null, CONTEXT_NAME);
			assertNotNull(updateContext);
			assertEquals("test", updateContext.name);
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testUpdateUnexistentContext(){
		try {
			LSContext updateContext = PlintRobot.getInstance().updateContext("xtest2", null, null, "NewDomainDescription", "xtest3");
			assertNotNull(updateContext);
			assertEquals("xtest2", updateContext.name);
		} catch (LintException e) {
			assertTrue(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(),true);
		}
	}

	@Test
	public void testGetContextInfo(){
		try {
			LSContext context = PlintRobot.getInstance().getContext(CONTEXT_NAME);
			assertNotNull(context);
			assertEquals("test", context.name);
		} catch (LintException e) {
			assertFalse(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertFalse(e.getMessage(),true);
		}
	}

	@Test
	public void testGetUnexistentContextInfo(){
		try {
			LSContext context = PlintRobot.getInstance().getContext("xpto");
			assertNull(context);
		} catch (LintException e) {
			assertTrue(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(),true);
		}
	}

	@Test
	public void testDeleteUnexistentContext(){
		try {
			assertFalse(PlintRobot.getInstance().deleteContext("xpto"));
		} catch (LintException e) {
			assertTrue(e.getMessage(),true);
		} catch (TimeoutException e) {
			assertTrue(e.getMessage(),true);
		}
	}
}
