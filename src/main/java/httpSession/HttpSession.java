package httpSession;

import java.util.HashMap;

public class HttpSession {
	private HashMap<String, Object> values = new HashMap<String, Object>();
	
	private String id;
	
	public HttpSession(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setAttribute(String name, Object value) {
		values.put(name,value);
	}
	
	public Object getAttribute(String name) {
		return values.get(name);
	}
	
	public void removeAttribute(String name) {
		values.remove(name);
	}
	
	public void invalidate() {
		HttpSessions.remove(id);
	}
}
