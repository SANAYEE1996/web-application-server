package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.controller.Controller;
import util.mapping.RequestMapping;

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
            if(request.getCookies().getCookie("JESSIONID") == null) {
            	response.addHeader("Set-Cookie", "JESSIONID="+UUID.randomUUID());
            }
            
            String requestUrl = request.getPath();
            Controller controller = RequestMapping.getController(requestUrl);
            log.debug("요청 온 url : {}",requestUrl);
            if(controller == null) {
            	response.forward(requestUrl);
            }
            else {
            	controller.service(request, response);
            }
            
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
}
