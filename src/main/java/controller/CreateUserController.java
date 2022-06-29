package controller;

import util.controller.AbstractController;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class CreateUserController extends AbstractController{
	HttpRequest request; 
	HttpResponse response;
	
	
	@Override
	public void service(HttpRequest request, HttpResponse response) {
		this.request = request;
		this.response = response;
		
		if(request.getMethod().equals("POST")) {
			doPost(request, response);
		}
		else if(request.getMethod().equals("GET")) {
			doGet(request, response);
			
		}
	}
	
	public void doPost(HttpRequest request, HttpResponse response) {
		System.out.println();
	}
	
	public void doGet(HttpRequest request, HttpResponse response) {
		System.out.println();
	}
	
}
