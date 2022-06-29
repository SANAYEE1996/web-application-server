package util.controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public abstract class AbstractController implements Controller{
	public abstract void doPost(HttpRequest request, HttpResponse response);
	public abstract void doGet(HttpRequest request, HttpResponse response);
}
