package com.tl.commerce.utils;


import cn.hutool.core.codec.Base64;

/**
 * 加密解密
 **/
public class CharCodeUtils {
    private CharCodeUtils(){

    }

    /**
     * encode
     * @param password
     * @return
     */
    public static String encode(String password){
        if(null==password||"".equals(password)) throw  new NullPointerException("password cannot be null");
        return Base64.encode(password);
    }

    /**
     * decode
     * @param password
     * @return
     */
    public static String decode(String password){
        if(null==password||"".equals(password)) throw  new NullPointerException("password cannot be null");
        return Base64.decodeStr(password);
    }
}
