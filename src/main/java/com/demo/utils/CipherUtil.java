package com.demo.utils;

import sun.misc.BASE64Decoder;

import java.security.MessageDigest;

/**
 * Created by Vonderland on 2017/3/11.
 */
public class CipherUtil {

    // BASE64解密
    public static String decodeData(String data) throws Exception{
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decodeContent = decoder.decodeBuffer(data);
        String decodeStr = new String(decodeContent);
        return decodeStr;
    }

    public static String encodeDataMD5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] mdMsg = md.digest(data.getBytes());
            return new String(mdMsg);
        } catch (Exception e) {
            return "";
        }
    }
}
