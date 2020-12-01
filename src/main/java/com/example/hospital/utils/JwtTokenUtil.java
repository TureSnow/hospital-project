package com.example.hospital.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Map;

/**
 * @author ftang
 */
@Component
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String secret = "token1q2w3e4r%^&";
    private static final long expiration = 100;

    public static String getToken(UserDetails userDetails){
        //7 day expired
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,7);

        JWTCreator.Builder builder = JWT.create();
        builder.withClaim("username",userDetails.getUsername());
        String token = builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(secret));
        return token;
    }

    public static void verifyToken(String token){
        JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }
}