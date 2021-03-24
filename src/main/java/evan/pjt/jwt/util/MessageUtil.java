package evan.pjt.jwt.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageUtil {

	ACCESS_TOKEN_COOKIE_IS_NULL(1,"access token cookie is null."),
	REFRESH_TOKEN_COOKIE_IS_NULL(2,"access token cookie is null."),
	ACCESS_TOKEN_IS_EXPIRED(3,"access token is expired."),
	REFRESH_TOKEN_IS_EXPIRED(4,"refresh token is expired.")
	;
	
	private final Integer id;
	
	private final String message;
	
}
