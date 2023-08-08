package com.hung.springbootserver.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${app.user-secret-key}")
    private String secret;
    //設置過期時間
    private final long expiration = 1800; // 單位:秒

    //生成token
    public String generateToken(String email){

        Date now =new Date();
        Date expirationDate = new Date(now.getTime() + (expiration * 1000));
//        Date expirationDate = new Date(now.getTime());

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //驗證token
    public boolean validateToken(String token, String email){

        Claims claims =null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if(claims.getSubject().equals(email)){
                return true;
            }
            else {
                return false;
            }
        }
        catch (ExpiredJwtException e){
            //如果token過期會噴出此例外，且返回false代表，驗證失敗
            System.out.println(e);
            return false;
        }
    }

}
