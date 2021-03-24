package evan.pjt.jwt.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	public final static long ACTK_VALIDATION_SECOND = 5;		// 3 second
	
	public final static long RETK_VALIDATION_SECOND = 10;	// 60 second

	private Key getSigningKey(String secretKey) {
		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public String doGenerateAccessToken(Integer uid) {
		
		Map<String, Object> header = new HashMap<>();
		header.put("alg","HS256");
		header.put("typ","JWT");
		
		Map<String, Object> payload = new HashMap<>();
		payload.put("localhost",true);
		payload.put("uid",uid);
		
		String jwt = Jwts.builder()
				.setHeader(header)
				.setClaims(payload)
				.setIssuer("localhost")
				.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
				.setExpiration(Timestamp.valueOf(LocalDateTime.now().plusSeconds(ACTK_VALIDATION_SECOND)))
				.signWith(getSigningKey("mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey"), SignatureAlgorithm.HS256)
				.compact();
		
		return jwt;
	}
	
	public String doGenerateRefreshToken(Integer uid) {
		
		Map<String, Object> header = new HashMap<>();
		header.put("alg","HS256");
		header.put("typ","JWT");
		
		Map<String, Object> payload = new HashMap<>();
		payload.put("localhost",true);
		payload.put("uid",uid);
		
		String jwt = Jwts.builder()
				.setHeader(header)
				.setClaims(payload)
				.setIssuer("localhost")
				.setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
				.setExpiration(Timestamp.valueOf(LocalDateTime.now().plusSeconds(RETK_VALIDATION_SECOND)))
				.signWith(getSigningKey("mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey"), SignatureAlgorithm.HS256)
				.compact();
		
		return jwt;
	}

	public Claims extractAllClaims(String token) throws ExpiredJwtException{ //
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey("mysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkeymysecretkey"))
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public Boolean isExpired(String token) {
		final Date expiration = extractAllClaims(token).getExpiration();
		return expiration.before(new Date());
	}
	
}
