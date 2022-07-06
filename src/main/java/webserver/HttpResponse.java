package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpResponse {
	
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	
	private byte[] body = null;
	private DataOutputStream dos;
	private HashMap<String, String> responseHeaderMap = new HashMap<>();
	private static final String message200 = "HTTP/1.1 200 OK \r\n";
	private static final String message302 = "HTTP/1.1 302 OK \r\n";
	
	
	public HttpResponse(OutputStream out) {
		this.dos = new DataOutputStream(out);
	}
	
	public void forward(String url) {
		if(url.equals("") || url.equals("/")) url = "/index.html";
		getHtml(url);
		response200Header();
		addHeader("Content-Type", "text/html;charset=utf-8");
		addHeader("Content-Length", String.valueOf(body.length));
		processHeaders();
		responseBody();
	}
	
	public void sendRedirect(String location) {
		getHtml(location);
		response302Header(location);
		addHeader("Content-Type", "text/html;charset=utf-8");
		addHeader("Content-Length", String.valueOf(body.length));
		processHeaders();
		responseBody();
	}
	
	public void forwardBody(String html) {
		log.debug("가져온 구조물 진짜임 ! : {}",html);
		body = html.getBytes();
		response200Header();
		addHeader("Content-Type", "text/html;charset=utf-8");
		addHeader("Content-Length", String.valueOf(body.length));
		processHeaders();
		responseBody();
	}
	
	//css forward response
	public void cssForward(String url) {
		getHtml(url);
		response200Header();
		responseCssHeader();
		responseBody();
	}
	
	public void getHtml(String url) {
		try {
			body = Files.readAllBytes(new File("./webapp" + url).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addHeader(String key, String value) {
		responseHeaderMap.put(key, value);
	}
	
	private void processHeaders() {
		try {
			for(String key : responseHeaderMap.keySet()) {
				dos.writeBytes(key);
				dos.writeBytes(": ");
				dos.writeBytes(responseHeaderMap.get(key));
				dos.writeBytes("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void responseCssHeader() {
		try {
			dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void response200Header() {
		try {
			dos.writeBytes(message200);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void response302Header(String location) {
		try {
			dos.writeBytes(message302);
			dos.writeBytes("Location: "+location);
			dos.writeBytes("\r\n");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void responseBody() {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
