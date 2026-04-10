package com.zhike.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String userId, String roleCode) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userId)
                .claim("roleCode", roleCode)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String generateToken(String userId, String roleCode, Map<String, Object> claims) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userId)
                .claim("roleCode", roleCode)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireTime)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    public String getRoleCode(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("roleCode");
    }

    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }

}