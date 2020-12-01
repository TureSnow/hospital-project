package com.example.hospital.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.hospital.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String,Object> map = new HashMap<>();
        String token = request.getHeader("token");
        try {
            JwtTokenUtil.verifyToken(token);
            return true;
        }catch (SignatureVerificationException e){
            map.put("msg","signature error!");
            e.printStackTrace();
        }catch (TokenExpiredException e){
            map.put("msg","expired!");
            e.printStackTrace();
        }catch (AlgorithmMismatchException e){
            map.put("msg","Algorithm Mismatch");
            e.printStackTrace();
        }catch (Exception e){
            map.put("msg","token 无效");
            e.printStackTrace();
        }
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
