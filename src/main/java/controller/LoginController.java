package controller;

import db.DataBase;
import model.User;
import util.controller.AbstractController;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class LoginController extends AbstractController{

	@Override
	public void service(HttpRequest request, HttpResponse reponse) {
		doPost(request, reponse);
	}

	public void doPost(HttpRequest request, HttpResponse response) {
		String requestId = request.getParameter("userId");
		String requestPw = request.getParameter("password");
		User user = DataBase.findUserById(requestId);
		if(user != null && 
		(user.getUserId().equals(requestId)	&& user.getPassword().equals(requestPw))) {
			response.addHeader("Set-Cookie", "logined=true");
			response.sendRedirect("/index.html");
		}
		else {
			response.addHeader("Set-Cookie", "logined=false");
			response.forward("/user/login_failed.html");
		}
		
	}

	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		
	}


}
