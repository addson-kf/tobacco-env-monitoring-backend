package org.example.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class JwtUtils
{
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    /**
     * 生成 JWT Token
     * @param userDetails 用户信息
     * @return JWT Token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // 添加用户角色信息（格式为 ROLE_XXX）
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 解析 JWT Token，获取 Claims
     * @param token JWT Token
     * @return Claims 对象
     * @throws JwtException Token 无效或已过期
     */
    public Claims parseToken(String token) throws JwtException {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token 已过期", e);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            throw new JwtException("无效的 Token", e);
        } catch (IllegalArgumentException e) {
            throw new JwtException("Token 为空", e);
        }
    }

    /**
     * 验证 Token 有效性
     * @param token JWT Token
     * @param userDetails 用户信息
     * @return 是否有效
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = getUsernameFromToken(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * 从 Token 中获取用户名
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }


    /**
     * 检查 Token 是否过期
     * @param token JWT Token
     * @return 是否过期
     */
    private boolean isTokenExpired(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());
    }
}

