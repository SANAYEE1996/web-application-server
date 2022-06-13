package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import model.User;

public class PostHandling {
	
	static boolean isRedirect = false;
	
	public byte[] bigHandling(String action, String readData) {
		if(action.equals("/user/create")) {
			return saveUser(readData);
		}
		return "준비중입니다".getBytes();
	}
	
	public byte[] goToHtml(String url) {
		byte[] body = null;
		try {
			body = Files.readAllBytes(new File("./webapp" + url).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}
	
	public byte[] saveUser(String readData) {
		
		String[] userArray = readData.split("&");
		String id = userArray[0].substring(userArray[0].indexOf("=") + 1);
		String password = userArray[1].substring(userArray[1].indexOf("=") + 1);
		String name = userArray[2].substring(userArray[2].indexOf("=") + 1);
		String email = userArray[3].substring(userArray[3].indexOf("=") + 1);
		
		User user = new User(id, password, name, email);
		
		System.out.println("id : " +user.getUserId());
		System.out.println("pw : " +user.getPassword());
		System.out.println("name : " +user.getName());
		System.out.println("email : " +user.getEmail());
		isRedirect = true;
		return goToHtml("/index.html");
	}
}
