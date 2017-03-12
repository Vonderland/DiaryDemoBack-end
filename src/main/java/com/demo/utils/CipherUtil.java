package com.demo.utils;

import sun.misc.BASE64Decoder;

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
}
