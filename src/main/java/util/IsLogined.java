package util;

import httpSession.HttpSession;
import util.session.JavaSessionUtils;

public class IsLogined {
	public static boolean islogined(HttpSession session) {
		if(session.getAttribute(JavaSessionUtils.JSESSIONID) == null) return false;
		return true;
	}
}
