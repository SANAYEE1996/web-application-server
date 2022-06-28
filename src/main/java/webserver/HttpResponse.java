package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	DataOutputStream dos;
	byte[] body;
	
	public HttpResponse(OutputStream out, byte[] body) {
		this.dos = new DataOutputStream(out);
		this.body = body;
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
