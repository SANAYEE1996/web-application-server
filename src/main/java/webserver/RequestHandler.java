package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        	System.out.println(out.toString());
        	InputStreamReader reader = new InputStreamReader(in);
        	BufferedReader br = new BufferedReader(reader);
        	Handling handling = new Handling();
        	
        	String line = br.readLine();
        	if(line == null) return;
        	byte[] body = "".getBytes();
        	String[] firstLine = line.split(" ");
        	String url = firstLine[1];
        	HashMap<String, String> map = new HashMap<>();
        	ReadLine.saveHttpToMap(map, br);
        	/* 
        	http 잘 가져오는지 map에 잘 저장했는지 확인하는 로그
        	for(String key : map.keySet()) 
        		log.debug("key->{}//content->{}",key,map.get(key));
        	 */
        	body = handling.getRequest(firstLine, map, br);
        	DataOutputStream dos = new DataOutputStream(out);
        	log.debug("!!! request url : {}" , firstLine[1]);
        	if(url.equals("/user/create")) {
        		response302Header(dos);
        		responseBody(dos, body); 
        	}
        	else if(url.equals("/user/login")) {
        		if(Handling.nowUrl.equals("/index.html")) {
        			response200Header_LoginSuccess(dos, body.length);
        			responseBody(dos, body); 
        		}
        		else if(Handling.nowUrl.equals("/user/login_failed.html")) {
        			response200Header_LoginFail(dos, body.length);
        			responseBody(dos, body); 
        		}
        	}
        	else if(url.equals("/css/styles.css") || 
        			url.equals("/css/bootstrap.min.css")) {
        		response200Header_Css(dos, body.length);
        		responseBody(dos, body); 
        	}
        	else {
        		response200Header(dos, body.length);
                responseBody(dos, body); 
        	}
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void response302Header(DataOutputStream dos) {
        String location = "/index.html";
    	try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void response200Header_Css(DataOutputStream dos, int lengthOfBodyContent) {
    	try {
    		dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void response200Header_LoginSuccess(DataOutputStream dos, int lengthOfBodyContent) {
    	String location = "/index.html";
    	try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location);
            dos.writeBytes("\r\n");
            dos.writeBytes("Set-Cookie: logined=true");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    
    private void response200Header_LoginFail(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Set-Cookie: logined=false");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
