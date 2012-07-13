package helpers;



public class Autority {

	public static boolean isAdmin() {
		return controllers.Lintity.currentContext().name.equals("admin");
	}
}
