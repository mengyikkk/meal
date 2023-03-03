package com.meal.common.utils;

import com.meal.common.config.TokenVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token工具类，用于生成token
 * 用户登录拿到Token然后访问我们的系统资源
 * @author ajie
 * @createTime 2021年07月22日 22:39:00
 */
@Component
public   class TokenUtils {

    @Value("${jwt.secret}")
    private  String secret;

    @Value("${jwt.expiration}")
    private  Long expiration;

    @Value("${jwt.refreshExpiration}")
    private  Long refreshExpiration;

    /**
     * 传入用户登录信息，生成token
     * @param details
     * @return
     */
    public TokenVo generateToken(UserDetails details) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        Map<String, Object> map = new HashMap<>(2);
        map.put("username", details.getUsername());
        map.put("created", new Date());
        return new TokenVo().setToken(generateJwt(map)).setRefresh(generateRefreshToken(map)).setExpire(expiration);
    }


    /**
     * 根据荷载信息去生成token
     * @param map
     * @return
     */
    private  String generateJwt(Map<String, Object> map) {
        return Jwts.builder()
                .setClaims(map)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000000))
                .compact();
    }
    private  String generateRefreshToken(Map<String, Object> map) {
        return Jwts.builder()
                .setClaims(map)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration * 1000000))
                .compact();
    }
    /**
     * 根据token获取荷载信息
     * @param token
     * @return
     */
    public Claims getTokenBody(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据token得到用户名
     * @param token
     * @return
     */
    public String getUsernameByToken(String token) {
        return (String) this.getTokenBody(token).get("username");
    }

    /**
     * 根据token判断当前时间内，该token是否过期
     * @param token
     * @return
     */
    public boolean isExpiration(String token) {
        return this.getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 刷新token令牌
     * @param token
     * @return
     */
    public TokenVo refreshToken(String token) {
        Claims claims = this.getTokenBody(token);
        claims.setExpiration(new Date());
        return new TokenVo().setExpire(expiration).setToken(this.generateJwt(claims));
    }
}
