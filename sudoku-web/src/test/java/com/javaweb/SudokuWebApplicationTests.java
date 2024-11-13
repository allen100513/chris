package com.javaweb;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class SudokuWebApplicationTests {


	@Test
	public void testGenJwt(){
		Map<String,Object> claims = new HashMap<>();
		claims.put("username","chris");
		claims.put("password","851");
		String token = Jwts.builder()
				.signWith(SignatureAlgorithm.HS256,"chris")
				.setClaims(claims)
				.setExpiration(new Date(System.currentTimeMillis()+60*60*1000))
				.compact();
		System.out.println(token);
	}
	@Test
	public void testParseJwt(){
		Claims claims = Jwts.parser()
				.setSigningKey("chris")
				.parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6Ijg1MTAwNSIsImV4cCI6MTcyOTc5ODc2OCwidXNlcm5hbWUiOiJjaHJpcyJ9.qI6TEVPHVLsmU2aoC9FNKfAEaHVdn4Ii6OInEn_9eBg")
				.getBody();
		System.out.println(claims);
	}

}
