package com.javaweb.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final String signKey = "chris";
    public static Long expire = 3600000L; // 1 hour

    public static String generateToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return token;
    }

    public static Claims parseToken(String token) {
        Claims claims = Jwts.parser()
               .setSigningKey(signKey)
               .parseClaimsJws(token)
               .getBody();
        return claims;
    }
}
