package controllers;

import java.util.List;

import models.Purchase;
import play.data.validation.Valid;
import play.i18n.Messages;

public class Purchases extends Application {
	public static void index() {
		List<Purchase> entities = models.Purchase.all().fetch();
		render(entities);
	}
}
