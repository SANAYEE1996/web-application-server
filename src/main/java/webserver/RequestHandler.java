package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.IOUtils;


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
        	
        	InputStreamReader reader = new InputStreamReader(in);
        	
        	
        	BufferedReader br = new BufferedReader(reader);
        	
        	
        	
        	String line = br.readLine();
        	if(line == null) return;
        	ControllerHandling ch = new ControllerHandling();
        	PostHandling ph = new PostHandling();
        	
        	
        	byte[] body = "".getBytes();
        	String[] str = line.split(" ");
        	String readContent = "";
        	int size = 0;
        	boolean doNotSplit = false;
        	String action = "";
        	boolean isRedirect = false;
        	
        	if(str[0].equals("GET")) {
				body = ch.getRequest(line);
				while(!"".equals(line)) {
	    			System.out.println(line);
					line = br.readLine();
	    		}
			}
        	else if (str[0].equals("POST")){
        		action = str[1];
        		while(!"".equals(line)) {
	    			line = br.readLine();
	    			if(!doNotSplit) str = line.split(" ");
	    			if(str[0].equals("Content-Length:")) {
	    				size = Integer.parseInt(str[1]);
	    				doNotSplit = true;
	    			}
	    		}
        		readContent = IOUtils.readData(br, size);
        		System.out.println("본문 : "+readContent);
        		body = ph.bigHandling(action, readContent);
        		isRedirect = PostHandling.isRedirect;
        	}
        	
        	
        	DataOutputStream dos = new DataOutputStream(out);
        	if (!isRedirect) {
        		response200Header(dos, body.length);
                responseBody(dos, body); 
			}
        	else {
        		response302Header(dos);
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
