package com.tl.commerce.utils;

import com.tl.commerce.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * jwt工具类
 */
public class JwtUtils {


    public static final String SUBJECT = "tl";

    public static final long EXPIRE = 1000*60*60*24*7;  //过期时间，毫秒，一周

    //秘钥
    public static final  String APPSECRET = "ASHF2372WFVF39KFKDF";

    /**
     * 生成jwt
     * @param user
     * @return
     */
    public static String geneJsonWebToken(User user){

        if(user == null || user.getId() == null || user.getLoginname() == null
                || user.getAvatar()==null){
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)//发行人
                .claim("id",user.getId())
                .claim("name",user.getLoginname())
                .claim("img",user.getAvatar())
                .setIssuedAt(new Date())//发行时间
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
