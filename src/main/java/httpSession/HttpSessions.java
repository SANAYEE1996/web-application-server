package httpSession;

import java.util.HashMap;

public class HttpSessions {
	public static HashMap<String, HttpSession> sessionValueMap = new HashMap<>();
	
	public static HttpSession getSession(String id) {
		HttpSession session = sessionValueMap.get(id);
		
		if(session == null) {
			session = new HttpSession(id);
			sessionValueMap.put(id, session);
		}
		
		return session;
	}
	
	public static void remove(String id) {
		sessionValueMap.remove(id);
	}
}
