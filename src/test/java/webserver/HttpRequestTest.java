package webserver;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.http.HttpRequest;

import org.junit.Test;

public class HttpRequestTest {
	
	private String testDirectory = "./src/test/resources/";

	@Test
	public void request_Get() throws Exception {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
//		HttpRequest request = new HttpRequest(in);
		
//		assertEquals("GET", request.getMethod());
		
		
	}

}
