package controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.controller.AbstractController;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class ListUserController extends AbstractController{
	
	private static final Logger log = LoggerFactory.getLogger(ListUserController.class);

	@Override
	public void service(HttpRequest request, HttpResponse reponse) {
		doGet(request,reponse);
	}

	
	public void doPost(HttpRequest request, HttpResponse response) {
		
	}

	
	public void doGet(HttpRequest request, HttpResponse response) {
		StringBuilder sb = new StringBuilder();
		Collection<User> users = DataBase.findAll();
		sb.append("<body>");
		sb.append("<h1> 현재 접속중인 접속자들 </h1>");
		for(User user : users) {
			sb.append("<div>");
			sb.append("user's id : " +user.getUserId());
			sb.append(" , user's name : " +user.getName());
			sb.append(" , user's email : " +user.getEmail());
			sb.append("</div>");
		}
		sb.append("</body>");
		log.debug("가져온 구조물 : {}",sb.toString());
		response.forwardBody(sb.toString());
	}

}
