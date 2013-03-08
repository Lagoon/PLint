package controllers;

import java.lang.reflect.InvocationTargetException;

import lagoon.PlintRobot;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import play.utils.Java;

/**
 * Manages access to controller - action
 * 
 * @author linda.velte
 * 
 */
public class Lint extends Controller {

	@Before
	static void checkAccess() throws Throwable {
		Lintity.invoke("beforeCheckAccess");

		if(getControllerAnnotation(Unsheltered.class) != null){
			Logger.debug("Unsheltered Controller :: " + request.action);
		}else if(getActionAnnotation(Unsheltered.class) != null){
			Logger.debug("Unsheltered Action :: " + request.action);
		}else if (getControllerInheritedAnnotation(Unsheltered.class) != null){
			Logger.debug("Unsheltered Inherited Controller :: " + request.action);
		}else{
			Long userID = (Long) Lintity.invoke("userId");
			if(userID == null){
				Lintity.invoke("uncheckedAccess");
			}else{
				String contextName = (String) Lintity.invoke("contextName");
				if (!PlintRobot.getInstance().checkRequest(request, userID, contextName)) {
					Lintity.invoke("onCheckFailed");
				}else{
					Lintity.invoke("onCheckSuccess");
				}
			}
		}
		Lintity.invoke("afterCheckAccess");
	}

	public static class Lintity extends Controller {

		/**
		 * This method is called when no User identification can be found.
		 */
		static void uncheckedAccess() {
			Logger.debug("No user id has found in session! PLint can not check request :: " + request.action);
			forbidden();
		}
		/**
		 * This method is called before check request access is verified.
		 */
		static void beforeCheckAccess() {
		}

		/**
		 * This method is called if a check does not succeed. By default it shows the not allowed page (the controller forbidden method).
		 */
		static void onCheckFailed() {
			Logger.debug("Access Denied to " + request.action);
			forbidden();
		}
		/**
		 * This method is called if a check succeed.
		 */
		static void onCheckSuccess() {
			Logger.debug("Access Granted to " + request.action);
		}

		/**
		 * This method is called after check request access is verified.
		 */
		static void afterCheckAccess() {
		}

		/**
		 * computes user id, if any
		 */
		static Long userId() {
			return 0l;
		}

		/**
		 * computes context name, if any
		 */
		static String contextName() {
			return null;
		}

		private static Object invoke(String m, Object... args) throws Throwable {

			try {
				return Java.invokeChildOrStatic(Lintity.class, m, args);
			} catch (InvocationTargetException e) {
				throw e.getTargetException();
			}
		}
	}
}
