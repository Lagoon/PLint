package controllers;

import java.util.Set;

import models.UserLagoon;
import models.UserPermission;

public class Permissions extends Application {
	public static void index(Long id) {
		UserLagoon userLagoon = UserLagoon.findById(id);
		Set<UserPermission> entities = userLagoon.userAccessPermission;
		render(entities);
	}
}
