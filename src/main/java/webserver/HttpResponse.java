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
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private DataOutputStream dos;
	private HashMap<String, String> responseHeaderMap;
	private static final String message200 = "HTTP/1.1 200 OK \\r\\n";
	private static final String message302 = "HTTP/1.1 302 OK \\r\\n";
	
	
	public HttpResponse(OutputStream out) {
		this.dos = new DataOutputStream(out);
	}
	
	
	public byte[] getHtml(String url) {
		byte[] body = null;
		try {
			body = Files.readAllBytes(new File("./webapp" + url).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}
	
	public void addHeader(String key, String value) {
		responseHeaderMap.put(key, value);
	}
	
	public void forward(String url) {
		if(url.contains("css")) {
			
		}
			
	}
	
	public void forwardBody() {
		
		
	}
	
	public void response200Header() {
		try {
			dos.writeBytes(message200);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void sendRedirect(String location) {
		try {
			dos.writeBytes(message302);
			dos.writeBytes("Location: "+location);
			dos.writeBytes("\r\n");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	public void processHeaders() {
		
		
	}
	
	
	
	private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
