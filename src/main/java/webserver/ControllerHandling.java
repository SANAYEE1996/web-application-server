package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ControllerHandling {
	public byte[] getRequest(String line) {
		String[] tokens = line.split(" ");
		System.out.println("type :\t"+tokens[0]);
		System.out.println("request :\t"+tokens[1]);
		if(tokens[1].equals("/")) {
			return goToIndex();
		}
		return "준비 중".getBytes();
	}
	
	public byte[] goToIndex() {
		byte[] body = null;
		try {
			body = Files.readAllBytes(new File("./webapp/index.html").toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}
}
