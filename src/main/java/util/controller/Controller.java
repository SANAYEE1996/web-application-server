package util.controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface Controller {
	void service(HttpRequest request, HttpResponse reponse);
}
