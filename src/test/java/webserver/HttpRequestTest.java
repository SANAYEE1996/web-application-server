package webserver;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;

public class HttpRequestTest {
	
	private String testDirectory = "./src/test/resources/";

	@Test
	public void request_Get() throws Exception {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_GET.txt"));
		webserver.HttpRequest request = new webserver.HttpRequest(in);
		
		assertEquals("GET", request.getMethod());
		assertEquals("/user/create", request.getPath());
		assertEquals("keep-alive", request.getHeader("Connection"));
		assertEquals("qwer", request.getParameter("id"));
		
	}
	
	@Test
	public void request_Post() throws Exception {
		InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
		webserver.HttpRequest request = new webserver.HttpRequest(in);
		
		assertEquals("POST", request.getMethod());
		assertEquals("/user/create", request.getPath());
		assertEquals("keep-alive", request.getHeader("Connection"));
		assertEquals("qwer", request.getParameter("id"));
		
	}

}
