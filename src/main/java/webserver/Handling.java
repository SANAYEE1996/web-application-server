package webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;


public class Handling {
	static String nowUrl;
	
	public byte[] getRequest(String[] firstLine, HashMap<String, String> map, BufferedReader br) throws IOException {
		
		if(firstLine[0].equals("GET")) {
			return getHandling(firstLine);
		}
		else if(firstLine[0].equals("POST")) {
			return postHandling(firstLine, map,br);
		}
		return "무엇인가 잘못 되었습니다.".getBytes();
	}
	
	public byte[] postHandling(	String[] firstLine, 
								HashMap<String, String> map,
								BufferedReader br) throws IOException {
		String url = firstLine[1];
		int contentLength = Integer.parseInt(map.get("Content-Length"));
		String readData = IOUtils.readData(br, contentLength);
		ArrayList<String> getParamList = HttpRequestUtils.getParam(readData);
		return postUrlMappint(url, getParamList);
	}
	
	public byte[] getHandling(String[] firstLine) {
		return goToHtml(getPath(firstLine[1]));
	}
	
	public byte[] postUrlMappint(String url, ArrayList<String> getParamList) {
		if(url.equals("/user/create")) return saveUser(getParamList);
		else if(url.equals("/user/login")) return loginUser(getParamList);
		return "아직 준비중인 url request".getBytes();
	}
	
	public byte[] loginUser(ArrayList<String> getParamList) {
		String requestId = getParamList.get(0);
		String requestPw = getParamList.get(1);
		
		User user = DataBase.findUserById(requestId);
		if(user != null && user.getUserId().equals(requestId) 
						&& user.getPassword().equals(requestPw)) {
			return goToHtml("/index.html");
		}
		return goToHtml("/user/login_failed.html");
	}
	
	public byte[] saveUser(ArrayList<String> getParamList) {
		
		User user = new User(	getParamList.get(0),getParamList.get(1),
								getParamList.get(2),getParamList.get(3));
		DataBase.addUser(user);
		return goToHtml("/index.html");
	}
	
	public String getPath(String url) {
		if(url.equals("/")) return "/index.html";
		return url;
	}
	
	public byte[] goToHtml(String url) {
		byte[] body = null;
		nowUrl = url;
		try {
			body = Files.readAllBytes(new File("./webapp" + url).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}
	
}
