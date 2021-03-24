package evan.pjt.jwt.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import evan.pjt.jwt.util.CookieUtil;
import evan.pjt.jwt.util.JwtUtil;
import evan.pjt.jwt.util.MessageUtil;
import evan.pjt.jwt.util.ResEntity;
import io.jsonwebtoken.ExpiredJwtException;

@RestController
public class JwtController {

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private CookieUtil cookieUtil;
	
	// access token 생성 후 쿠키 등록 
	@GetMapping("/jwt/actk/create")
	ResponseEntity<?> createJwtActk(HttpServletRequest request, HttpServletResponse response){
		String actk = jwtUtil.doGenerateAccessToken(3667);
		String retk = jwtUtil.doGenerateRefreshToken(3667);
		response.addCookie(cookieUtil.createCookie("evan_actk", actk, 180));
		response.addCookie(cookieUtil.createCookie("evan_retk", retk, 180));
		return ResEntity.success(actk);
	}
	
	//	쿠키, 토큰 만료여부 조회 
	//	1. access token 만료 시
	//		1) refresh token 살아있으면 	-> access token 재발행
	// 		2) refresh token 만료되면		-> 종료, 더이상의 재발행 없음. (사용자 인증 시 재발행 해야하나, 별도의 인증로직은 아직 구현되지 않음.) 
	
	@GetMapping("/jwt/actk/cookie")
	ResponseEntity<?> getJwtActkCookie(HttpServletRequest request, HttpServletResponse response){
		// 쿠키 유효시간이 토큰 만료시간보다 길다는 가정 하에...
		try {
			
			Cookie actkCookie = cookieUtil.getCookie(request, "evan_actk");
			
			if(actkCookie == null)
				return ResEntity.fail(HttpStatus.UNAUTHORIZED, MessageUtil.ACCESS_TOKEN_COOKIE_IS_NULL); 
			
			Boolean jwtIsExpired = jwtUtil.isExpired(actkCookie.getValue());
			
			Map<String, Object> map = new HashMap<>();
			map.put("jwt",actkCookie.getValue());
			map.put("expired",jwtIsExpired);
			
			return ResEntity.success(map);
			
		} catch (ExpiredJwtException e) {
			
			Cookie retkCookie = cookieUtil.getCookie(request, "evan_retk");
			
			try {
			
				if(retkCookie == null)
					return ResEntity.fail(HttpStatus.UNAUTHORIZED, MessageUtil.REFRESH_TOKEN_COOKIE_IS_NULL);
				
				Boolean retkIsExpired = jwtUtil.isExpired(retkCookie.getValue());
				
				String actk = jwtUtil.doGenerateAccessToken(3667);
				response.addCookie(cookieUtil.createCookie("evan_actk", actk, 180));
				
			} catch (ExpiredJwtException e2) {
				
				return ResEntity.fail(HttpStatus.UNAUTHORIZED, MessageUtil.REFRESH_TOKEN_IS_EXPIRED);
				
			}
				
			return ResEntity.success(MessageUtil.ACCESS_TOKEN_IS_EXPIRED);
			
		}
		
	}
	
}
