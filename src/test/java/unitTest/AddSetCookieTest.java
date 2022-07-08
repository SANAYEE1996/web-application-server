package unitTest;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import org.junit.Test;

import webserver.HttpResponse;

public class AddSetCookieTest {
	
	private String testDirectory = "./src/test/resources/";
	
	@Test
	public void addCookies() throws FileNotFoundException {
		HttpResponse response = new HttpResponse(createOutputStream("Http_Cookie.txt"));
		response.addHeader("Set-Cookie", "logined=true");
		UUID uuid = UUID.randomUUID();
        response.addHeader("Set-Cookie", "JSESSIONID="+uuid.toString());
		response.sendRedirect("/index.html");
	}
	
	private OutputStream createOutputStream(String filename) throws FileNotFoundException{
		return new FileOutputStream(new File(testDirectory + filename));
	}

}
