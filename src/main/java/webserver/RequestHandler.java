package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", 
        								connection.getInetAddress(),
        								connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);
            log.debug("요청 온 url : {}",request.getPath());
            if(request.getPath().equals("/user/create")) {
            	CreateUserController c = new CreateUserController();
            	c.service(request, response);
            }
            else if(request.getPath().equals("/user/list")) {
            	ListUserController c = new ListUserController();
            	c.service(request, response);
            }
            else if(request.getPath().equals("/user/login")) {
            	LoginController c = new LoginController();
            	c.service(request, response);
            }
            else if(request.getPath().contains("css")) {
            	response.cssForward(request.getPath());
            }
            else {
            	response.forward(request.getPath());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
}
