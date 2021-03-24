package evan.pjt.jwt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

	public Cookie createCookie(String cookeName, String value, Integer validSecond) {
		Cookie cookie = new Cookie(cookeName, value);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(validSecond);
		cookie.setPath("/");
		return cookie;
	}
	
	public Cookie getCookie(HttpServletRequest request, String cookieName) {
		final Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			return null;
		}
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(cookieName)) {
				return cookie;
			}
		}
		return null;
	}
	
	
}
