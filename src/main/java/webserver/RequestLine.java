package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import util.HttpMethod;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestLine {
	
	private HttpMethod method;
	private BufferedReader reader = null;
	private String[] first;
	private HashMap<String, String> httpHeaderMap = new HashMap<>();
	private HashMap<String, String> parameterMap = new HashMap<>();
	
	public RequestLine(BufferedReader reader) {
		try {
			this.reader = reader;
			this.first = reader.readLine().split(" ");
			method = HttpMethod.valueOf(getMethod());
			readHeader();
			saveParam();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveParam() {
		if(method.isPost()) {
			readBody();
		}
		else if(method.isGet()) {
			readGet(first[1]);
		}
	}
	
	private void readHeader() {
		try {
			String line = reader.readLine();
			int index = 0;
			while(!"".equals(line) && line != null) {
				index = line.indexOf(":");
				if(index >= 0) httpHeaderMap.put(line.substring(0, index).trim(), line.substring(index+1).trim());
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readGet(String url) {
		int index = url.indexOf("?");
		if(index < 0) parameterMap = null;
		else parameterMap = (HashMap<String, String>) HttpRequestUtils.parseQueryString(url.substring(index+1));
	}
	
	private void readBody() {
		int contentLength = Integer.parseInt(httpHeaderMap.get("Content-Length"));
		String[] readData;
		try {
			readData = IOUtils.readData(reader, contentLength).split("&");
			int andIndex = 0;
			for(String s : readData) {
				andIndex = s.indexOf("=");
				parameterMap.put(s.substring(0, andIndex),s.substring(andIndex+1));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getMethod() {
		return first[0];
	}
	
	public String getPath() {
		int index = first[1].indexOf("?");
		if(index < 0) return first[1];
		return first[1].substring(0, index);
	}
	
	public HashMap<String, String> getHttpHeaderMap(){
		return httpHeaderMap;
	}
	
	public HashMap<String, String> getParameterMap(){
		return parameterMap;
	}
	
}
