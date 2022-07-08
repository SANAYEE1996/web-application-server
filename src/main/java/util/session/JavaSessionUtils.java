package util.session;

import httpSession.HttpSession;
import model.User;

public class JavaSessionUtils {
	public static final String JSESSIONID = "user";
	
	public static User getUserFromSession(HttpSession session) {
        Object user = session.getAttribute(JSESSIONID);
        if (user == null) {
            return null;
        }
        return (User) user;
    }

    public static boolean isLogined(HttpSession session) {
        if (getUserFromSession(session) == null) {
            return false;
        }
        return true;
    }

    public static boolean isSameUser(HttpSession session, User user) {
        if (!isLogined(session)) {
            return false;
        }

        if (user == null) {
            return false;
        }

        return user.isSameUser(getUserFromSession(session));
    }
}
