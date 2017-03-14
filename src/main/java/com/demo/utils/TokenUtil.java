package com.demo.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Vonderland on 2017/3/11.
 */
public class TokenUtil {

    public static String generateToken(long uid) {
        HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("uid", Long.toString(uid));
        claims.put("iat", System.currentTimeMillis());
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, "Ctd.Vonderland")
                .compact();
        return token;
    }

    public static long getUidFromToken(String token) {
        String uid = (String) Jwts.parser()
                .setSigningKey("Ctd.Vonderland")
                .parseClaimsJws(token)
                .getBody()
                .get("uid");
        return Long.parseLong(uid);
    }

    public static String generateRandomPassword() {
        StringBuilder tmp = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        for(int i = 0; i < 8; i++) {
            int choice = random.nextInt(3);
            if (choice == 0) {
                tmp.append((char)(random.nextInt(26) + 'a'));
            } else if (choice == 1) {
                tmp.append((char)(random.nextInt(10) + '0'));
            } else {
                tmp.append((char)(random.nextInt(26) + 'A'));
            }
        }
        return tmp.toString();
    }
}
