package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpResponse {
	
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	
	private byte[] body = null;
	private DataOutputStream dos;
	private ArrayList<String> responseHeaderList = new ArrayList<>();
	private static final String message200 = "HTTP/1.1 200 OK \r\n";
	private static final String message302 = "HTTP/1.1 302 Found \r\n";
	
	
	public HttpResponse(OutputStream out) {
		this.dos = new DataOutputStream(out);
		responseHeaderList.add(message200);	//default로 응답 머리 리스트 추가
		responseHeaderList.add(message302); //default로 응답 머리 리스트 추가
	}
	
	public void forward(String url) {
		responseHeaderList.remove(1);			//302를 삭제
		if(url.equals("") || url.equals("/")) url = "/index.html";
		getHtml(url);
		if(url.endsWith(".css")) {
			addHeader("Content-Type", "text/css;charset=utf-8");
			addHeader("Content-Length", String.valueOf(body.length)+"\r\n");
		}
		else if(url.endsWith(".js")) {
			addHeader("Content-Type", "application/javascript");
			addHeader("Content-Length", String.valueOf(body.length));
		}
		else {
			addHeader("Content-Type", "text/html;charset=utf-8");
			addHeader("Content-Length", String.valueOf(body.length));
		}
		responseHeader();
		responseBody();
	}
	
	public void sendRedirect(String location) {
		responseHeaderList.remove(0);			//200을 삭제하고,
		getHtml(location);
		addHeader("Location", location);		//redirect location 추가
		addHeader("Content-Type", "text/html;charset=utf-8");
		addHeader("Content-Length", String.valueOf(body.length));
		responseHeader();
		responseBody();
	}
	
	public void forwardBody(String html) {
		responseHeaderList.remove(1);		//302를 삭제
		body = html.getBytes();
		addHeader("Content-Type", "text/html;charset=utf-8");
		addHeader("Content-Length", String.valueOf(body.length));
		responseHeader();
		responseBody();
	}
	
	
	public void getHtml(String url) {
		try {
			body = Files.readAllBytes(new File("./webapp" + url).toPath());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	public void addHeader(String key, String value) {
		responseHeaderList.add(key + ": " + value+"\r\n");
	}
	
	
	private void responseHeader() {
		try {
            for(String s : responseHeaderList) {
            	dos.writeBytes(s);
            }
        } catch (IOException e) {
        	log.error(e.getMessage());
        }
	}
	
	private void responseBody() {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
        	log.error(e.getMessage());
        }
    }
}
