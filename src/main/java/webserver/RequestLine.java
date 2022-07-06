package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpMethod;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestLine {
	
	private static final Logger log = LoggerFactory.getLogger(RequestLine.class);
	
	private HttpMethod method;
	private BufferedReader reader = null;
	private String[] first = null;
	private String requestUrl = null;
	private HashMap<String, String> httpHeaderMap = new HashMap<>();
	private HashMap<String, String> parameterMap = new HashMap<>();
	
	public RequestLine(BufferedReader read) {
		try {
			reader = read;
			first = reader.readLine().split(" ");
			method = HttpMethod.valueOf(first[0]);
			requestUrl = first[1];
			readHeader();
			saveParam();
			
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void saveParam() {
		if(method.isPost()) {
			readBody();
		}
		else if(method.isGet()) {
			readGet(requestUrl);
		}
	}
	
	private void saveHeaderToMap(int index, String line) {
		try {
			while(!"".equals(line) && line != null) {
				index = line.indexOf(":");
				if(index >= 0) httpHeaderMap.put(line.substring(0, index).trim(), line.substring(index+1).trim());
				line = reader.readLine();
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void readHeader() {
		try {
			String line = reader.readLine();
			saveHeaderToMap(0, line);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void readGet(String url) {
		int index = url.indexOf("?");
		if(index < 0) parameterMap = null;
		else parameterMap = (HashMap<String, String>) HttpRequestUtils.parseQueryString(url.substring(index+1));
	}
	
	private void saveBodyToMap(int andIndex, String[] readData) {
		for(String s : readData) {
			andIndex = s.indexOf("=");
			parameterMap.put(s.substring(0, andIndex),s.substring(andIndex+1));
		}
	}
	
	private void readBody() {
		int contentLength = Integer.parseInt(httpHeaderMap.get("Content-Length"));
		String[] readData;
		try {
			readData = IOUtils.readData(reader, contentLength).split("&");
			saveBodyToMap(0, readData);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	public HttpMethod getMethod() {
		return method;
	}
	
	public String getPath() {
		int indexOfParameterSplit = requestUrl.indexOf("?");
		if(indexOfParameterSplit < 0) return requestUrl;
		return requestUrl.substring(0, indexOfParameterSplit);
	}
	
	public HashMap<String, String> getHttpHeaderMap(){
		return httpHeaderMap;
	}
	
	public HashMap<String, String> getParameterMap(){
		return parameterMap;
	}
	
}
