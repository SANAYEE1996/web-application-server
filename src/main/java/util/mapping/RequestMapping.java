package util.mapping;

import java.util.LinkedHashMap;
import java.util.Map;

import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;
import util.controller.Controller;

public class RequestMapping {
	private static Map<String, Controller> controllers = new LinkedHashMap<>();
	
	static {
		controllers.put("/user/create", new CreateUserController());
		controllers.put("/user/login", new LoginController());
		controllers.put("/user/list", new ListUserController());
	}
	
	public static Controller getController(String requestUrl) {
		return controllers.get(requestUrl);
	}
}
