package webserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import util.HttpMethod;

public class HttpRequest {
	
	private HashMap<String, String> headerMap = new HashMap<>();
	private HashMap<String, String> parameterMap = new HashMap<>();
	private RequestLine line;
	
	public HttpRequest(InputStream http){
		InputStreamReader reader = new InputStreamReader(http);
		BufferedReader br = new BufferedReader(reader);
		line = new RequestLine(br);
		headerMap = line.getHttpHeaderMap();
		parameterMap = line.getParameterMap();
	}
	
	public HttpMethod getMethod() {
		return line.getMethod();
	}
	
	public String getPath() {
		return line.getPath();
	}
	
	public String getHeader(String key) {
		return headerMap.get(key);
	}
	
	public String getParameter(String key) {
		return parameterMap.get(key);
	}
	
	
}
