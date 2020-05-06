package com.example.demo.util.jwt;

import com.example.demo.domain.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * jwt工具类
 */
public class JwtUtils {

    public static final String SUBJECT = "";
    //过期时间，毫秒，一周
    public static final long EXPIRE = 1000*60*60*24*7;
    //秘钥
    public static final  String APPSECRET = "xd666";

    /**
     * 生成jwt
     * @param user
     * @return
     */
    public static String geneJsonWebToken(Users user){
        if(user == null || user.getId() == null || user.getUsername()== null){
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id",user.getId())
                .claim("name",user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE))
                .signWith(SignatureAlgorithm.HS256,APPSECRET).compact();

        return token;
    }
    /**
     * 校验token
     * @param token
     * @return
     */
    public static Claims checkJWT(String token ){
        try{
            final Claims claims =  Jwts.parser().setSigningKey(APPSECRET).
                    parseClaimsJws(token).getBody();
            return  claims;
        }catch (Exception e){ }
        return null;
    }
}
