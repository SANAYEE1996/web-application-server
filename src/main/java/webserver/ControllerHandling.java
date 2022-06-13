package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class ControllerHandling {
	public byte[] getRequest(String line) {
		String[] tokens = line.split(" ");
		
		System.out.println("from : "+line);
		if(tokens.length > 1) {
			return goToHtml(getPath(tokens[1]));
		}
		return "무엇인가 잘못 되었습니다.".getBytes();
	}
	
	public String getPath(String tokens) {
		if(tokens.equals("/")) return "/index.html";
		return tokens;
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
	
}
