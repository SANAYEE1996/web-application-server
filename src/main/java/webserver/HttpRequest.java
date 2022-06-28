package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.IOUtils;

public class HttpRequest {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	InputStream in;
	InputStreamReader reader;
	BufferedReader br;
	String[] requestArray;
	String queryString;
	String url;
	String path;
	int queryIndex;
	HashMap<String, String> httpHeaderMap;
	HashMap<String, String> getParameterMap;
	HashMap<String, String> postParameterMap;
	
	public HttpRequest(InputStream http) throws IOException {
		this.in = http;
		this.reader = new InputStreamReader(in);
		this.br = new BufferedReader(reader);
		this.requestArray = br.readLine().split(" ");
		this.url = requestArray[1];
		this.queryIndex = url.indexOf("?");
		this.path = (queryIndex >= 0) ? url.substring(0,queryIndex) : url;
		this.queryString = (queryIndex >= 0) ? url.substring(queryIndex+1) : "";
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
		this.postParameterMap = new HashMap<>();
		for(String s : readData) {
			andIndex = s.indexOf("=");
			postParameterMap.put(s.substring(0, andIndex),s.substring(andIndex+1));
		}
	}
	
	public void saveGetParameterToMap() throws IOException {
		String[] str = queryString.split("&");
		int andIndex = 0;
		this.getParameterMap = new HashMap<>();
		for(String s : str) {
			andIndex = s.indexOf("=");
			getParameterMap.put(s.substring(0, andIndex),s.substring(andIndex+1));
		}
	}

	public void saveHttpToMap() throws IOException {
		String line = br.readLine();
		int index = 0;
		this.httpHeaderMap = new HashMap<>();
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
		log.debug("path : {}", path);
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
