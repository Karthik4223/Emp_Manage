package com.example.employee.helpers;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    private final String SECRET_KEY = "my-super-secret-key-should-be-long-and-secure";

    private Key getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(String empCode, String empName, List<String> rights) {
        return Jwts.builder()
            .setSubject(empCode)
            .claim("empName", empName)
            .claim("authorities", rights)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  
            .signWith(getSigningKey())
            .compact();
    }


    public String extractEmpCode(String token) {
        return getClaims(token).getSubject();
    }

    public String extractEmpName(String token) {
        return getClaims(token).get("empName", String.class);
    }

    public boolean validateToken(String token, String empCode) {
        String tokenEmpCode = extractEmpCode(token);
        return (tokenEmpCode.equals(empCode) && !isTokenExpired(token));
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
