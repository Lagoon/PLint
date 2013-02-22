package controllers;

import java.util.concurrent.TimeoutException;

import play.Logger;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Util;
import play.mvc.With;
import exceptions.LintException;

@With(SecureLint.class)
public class Application extends Controller {

	public static void index() throws LintException, TimeoutException{
		render();
	}

	@Catch(value = LintException.class, priority = 1)
	public static void lintException(Throwable throwable) throws LintException, TimeoutException {
		doFlashError(throwable.getMessage());
		index();
	}

	@Catch(value = TimeoutException.class, priority = 1)
	public static void timeoutException(Throwable throwable) throws LintException, TimeoutException {
		doFlashError("Connection timed out");
		index();
	}

	@Util
	public static void doFlashError(String message) {

		if (message != null) {
			if (message.contains("::")) {
				message = message.substring(message.indexOf("::") + 2, message.length());
			}
			flash.error(message);
		}
		Logger.info("ModuleException :: %s", message);
	}

	@Util
	public static void doFlashSuccess(String message) {
		flash.success(message);
	}
}