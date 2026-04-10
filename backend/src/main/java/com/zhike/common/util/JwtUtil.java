package com.zhike.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 * 提供JWT Token的生成、解析和验证功能
 */
@Component
public class JwtUtil {

    /**
     * JWT密钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Token过期时间（毫秒）
     */
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * 生成JWT Token
     * @param userId 用户ID
     * @param roleCode 角色代码
     * @return JWT Token字符串
     */
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

    /**
     * 生成JWT Token（包含额外声明）
     * @param userId 用户ID
     * @param roleCode 角色代码
     * @param claims 额外的声明
     * @return JWT Token字符串
     */
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

    /**
     * 解析JWT Token
     * @param token JWT Token字符串
     * @return Token中的声明
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从Token中获取用户ID
     * @param token JWT Token字符串
     * @return 用户ID
     */
    public String getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 从Token中获取角色代码
     * @param token JWT Token字符串
     * @return 角色代码
     */
    public String getRoleCode(String token) {
        Claims claims = parseToken(token);
        return (String) claims.get("roleCode");
    }

    /**
     * 检查Token是否已过期
     * @param token JWT Token字符串
     * @return true表示已过期，false表示未过期
     */
    public boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }

}