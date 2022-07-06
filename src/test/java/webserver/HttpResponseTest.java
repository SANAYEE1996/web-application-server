package webserver;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;

import org.junit.Test;

import db.DataBase;
import model.User;

public class HttpResponseTest {
	private String testDirectory = "./src/test/resources/";
	
	@Test
	public void responseForwardTest() throws FileNotFoundException {
		HttpResponse response = new HttpResponse(createOutputStream("Http_Forward.txt"));
		response.forward("/index.html");
	}
	
	@Test
	public void responseRedirectTest() throws FileNotFoundException {
		HttpResponse response = new HttpResponse(createOutputStream("Http_Redirect.txt"));
		response.sendRedirect("/index.html");
	}
	
	@Test
	public void responseCookies() throws FileNotFoundException {
		HttpResponse response = new HttpResponse(createOutputStream("Http_Cookie.txt"));
		response.addHeader("Set-Cookie", "logined=true");
		response.sendRedirect("/index.html");
	}
	
	@Test
	public void responseUserList() throws FileNotFoundException {
		HttpResponse response = new HttpResponse(createOutputStream("Http_user_list_Request.txt"));
		StringBuilder sb = new StringBuilder();
		Collection<User> users = DataBase.findAll();
		sb.append("<body>");
		sb.append("<h1> 현재 접속중인 접속자들 </h1>");
		for(User user : users) {
			sb.append("<div>");
			sb.append("user's id : " +user.getUserId());
			sb.append(" , user's name : " +user.getName());
			sb.append(" , user's email : " +user.getEmail());
			sb.append("</div>");
		}
		sb.append("</body>");
		response.forwardBody(sb.toString());
	}
	
	@Test
	public void cssTest() throws FileNotFoundException {
		HttpResponse response = new HttpResponse(createOutputStream("Http_Css.txt"));
		response.cssForward("/css/styles.css");
	}
	
	@Test
	public void bootStrapTest() throws FileNotFoundException {
		HttpResponse response = new HttpResponse(createOutputStream("Http_BootStrap.txt"));
		response.cssForward("/css/bootstrap.min.css");
	}
	
	
	private OutputStream createOutputStream(String filename) throws FileNotFoundException{
		return new FileOutputStream(new File(testDirectory + filename));
	}

}
