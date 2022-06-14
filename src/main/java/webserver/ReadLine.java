package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class ReadLine {
	public static void saveHttpToMap(HashMap<String,String> map, BufferedReader br) throws IOException {
		String line = br.readLine();
		int index = 0;
		while(!"".equals(line)) {
			index = line.indexOf(":");
			if(index >= 0) map.put(line.substring(0, index), line.substring(index+2));
			line = br.readLine();
		}
	}

}
