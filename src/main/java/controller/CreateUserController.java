package controller;

import db.DataBase;
import model.User;
import util.controller.AbstractController;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class CreateUserController extends AbstractController{
	
	
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		
		if(request.getMethod().equals("POST")) {
			doPost(request, response);
		}
		else if(request.getMethod().equals("GET")) {
			doGet(request, response);
		}
	}
	
	public void doPost(HttpRequest request, HttpResponse response) {
		User user = new User( request.getParameter("userId"),
							  request.getParameter("password"),
							  request.getParameter("name"),
							  request.getParameter("email"));
		DataBase.addUser(user);
		response.sendRedirect("/index.html");
	}
	
	public void doGet(HttpRequest request, HttpResponse response) {
		
	}
	
}
