package webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;


public class Handling {
	static String nowUrl;
	
	
	public byte[] getRequest(String[] firstLine, HashMap<String, String> map, BufferedReader br) throws IOException {
		String url = firstLine[1];
		if(firstLine[0].equals("GET")) {
			return getHandling(url,map);
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
		return postUrlMappint(url, getParamList,map);
	}
	
	public byte[] getHandling(String url, HashMap<String, String> map) {
		if(url.equals("/user/list")) return showUserList(map);
		return goToHtml(getPath(url));
	}
	
	public byte[] postUrlMappint(String url, ArrayList<String> getParamList, HashMap<String, String> map) {
		if(url.equals("/user/create")) return saveUser(getParamList);
		else if(url.equals("/user/login")) return loginUser(getParamList);
		else if(url.equals("/user/list")) return showUserList(getParamList,map);
		return "아직 준비중인 url request".getBytes();
	}
	
	public byte[] showUserList() {
		StringBuilder sb = new StringBuilder();
		User users = (User) DataBase.findAll();
		
		return "사용자가 왜 아무도 없지..".getBytes();
	}
	
	public byte[] showUserList(HashMap<String, String> map) {
		String cookies = map.get("Cookie");
		Map<String, String> cookieMap= HttpRequestUtils.parseCookies(cookies);
		if(cookieMap.get("logined").contains("true"))	return "사용자 목록".getBytes();
		return goToHtml("/user/login.html");
	}
	
	public byte[] showUserList(ArrayList<String> getParamList, HashMap<String, String> map) {
		String cookies = map.get("Cookie");
		Map<String, String> cookieMap= HttpRequestUtils.parseCookies(cookies);
		if(cookieMap.get("logined").contains("true"))	return showUserList();
		return goToHtml("/user/login.html");
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
