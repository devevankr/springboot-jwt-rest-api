package evan.pjt.jwt.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResEntity {

	public static <T> ResponseEntity<?> success(T t){
		return ResponseEntity.status(HttpStatus.OK).body(t);
	}
	
	public static <T> ResponseEntity<?> fail(HttpStatus httpStatus, T t){
		Map<String, T> map = new HashMap<>();
		map.put("message", t);
		return ResponseEntity.status(httpStatus).body(map);
	}
	
	public static <T> ResponseEntity<?> error(T t){
		Map<String, T> map = new HashMap<>();
		map.put("message", t);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
	}
	
}
