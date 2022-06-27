package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import util.IOUtils;

public class HttpRequest {
	InputStream in = null;
	InputStreamReader reader = null;
	BufferedReader br = null;
	String[] requestArray;
	String queryString;
	String url;
	String path;
	int queryIndex = 0;
	HashMap<String, String> httpHeaderMap = new HashMap<>();
	HashMap<String, String> getParameterMap = new HashMap<>();
	HashMap<String, String> postParameterMap = new HashMap<>();
	
	public HttpRequest(InputStream http) throws IOException {
		this.in = http;
		reader = new InputStreamReader(in);
		br = new BufferedReader(reader);
		requestArray = br.readLine().split(" ");
		url = requestArray[1];
		queryIndex = url.indexOf("?");
		path = (queryIndex >= 0) ? url.substring(0,queryIndex) : url;
		queryString = (queryIndex >= 0) ? url.substring(queryIndex+1) : "";
		saveHttpToMap();
		if (getMethod().equals("GET")) {
			saveGetParameterToMap();
		}
		else if(getMethod().equals("POST")) {
			savePostParameterToMap();
		}
	}
	
	public void savePostParameterToMap() throws IOException{
		int contentLength = Integer.parseInt(httpHeaderMap.get("Content-Length"));
		String[] readData = IOUtils.readData(br, contentLength).split("&");
		int andIndex = 0;
		for(String s : readData) {
			andIndex = s.indexOf("=");
			postParameterMap.put(s.substring(0, andIndex),s.substring(andIndex+1));
		}
	}
	
	public void saveGetParameterToMap() throws IOException {
		String[] str = queryString.split("&");
		int andIndex = 0;
		for(String s : str) {
			andIndex = s.indexOf("=");
			getParameterMap.put(s.substring(0, andIndex),s.substring(andIndex+1));
		}
	}

	public void saveHttpToMap() throws IOException {
		String line = br.readLine();
		int index = 0;
		while(!"".equals(line) && line != null) {
			System.out.println(line);
			index = line.indexOf(":");
			if(index >= 0) httpHeaderMap.put(line.substring(0, index).trim(), line.substring(index+1).trim());
			line = br.readLine();
		}
	}
	
	public String getMethod() {
		return requestArray[0];
	}
	
	public String getPath() {
		return path;
	}
	
	public String getHeader(String key) {
		return httpHeaderMap.get(key);
	}
	
	public String getParameter(String key) {
		if(getMethod().equals("GET")) {
			return getParameterMap.get(key);
		}
		else if(getMethod().equals("POST")) {
			return postParameterMap.get(key);
		}
		return "http method Error";
	}
	
}
