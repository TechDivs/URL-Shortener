package com.divs.urlShortener.interceptor;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String,String> redisTemplate;
    public RateLimitInterceptor(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception {
        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();

        if(path.contains("/test") || path.contains("/top-urls")) {
            return true;
        }

        boolean isCreate = path.contains("/shorten");
        int limit = isCreate ? 10 : 200;
        String keyPrefix = isCreate ? "rate:create:" : "rate:get:";

        String key = keyPrefix + ip;
        Long count = redisTemplate.opsForValue().increment(key);

        if(count == 1) {
            redisTemplate.expire(key, 60, TimeUnit.SECONDS);
        }

        if(count > limit) {
            response.setStatus(429);
            response.getWriter().write("Too many requests !");
            return false;
        }
        
        return true;
    }
}
