package com.divs.urlShortener.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.divs.urlShortener.dto.TopUrlResponse;
import com.divs.urlShortener.model.Url;
import com.divs.urlShortener.repository.UrlRepository;


@Service
public class UrlService {
    private final UrlRepository urlRepository;
    private final RedisTemplate<String,String> redisTemplate;
    
    public UrlService(UrlRepository urlRepository, RedisTemplate<String,String> redisTemplate) {
        this.urlRepository=urlRepository;
        this.redisTemplate=redisTemplate;
    }

    public String createShortUrl(String originalUrl, long ttl) {
        ttl = Math.min(ttl, 86400);
        Url url = new Url();
        Instant now = Instant.now();
        
        url.setOriginalUrl(originalUrl);
        url.setCreatedAt(now);
        url.setClickCount(0l);
        url.setExpiresAt(now.plusSeconds(ttl));
        url = urlRepository.save(url);

        String hex = url.getId().toHexString();
        hex = hex.substring(hex.length() - 12);
        String shortCode = encodeBase62(hex);
        
        url.setShortCode(shortCode);
        urlRepository.save(url);
        return shortCode;
    }


    private String encodeBase62(String hex) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        java.math.BigInteger num = new java.math.BigInteger(hex, 16);
        StringBuilder sb = new StringBuilder();
        java.math.BigInteger base = java.math.BigInteger.valueOf(62);

        while(num.compareTo(java.math.BigInteger.ZERO) > 0) {
            int rem = num.mod(base).intValue();
            sb.append(chars.charAt(rem));
            num = num.divide(base);
        }

        return sb.reverse().toString();
    }


    public String getOriginalString(String shortCode) {
        String key = "url:" + shortCode;
        String cached = redisTemplate.opsForValue().get(key);

        if(cached!=null) {
            // System.out.println("Found in redis: " + cached);
            redisTemplate.opsForValue().increment("clicks:"+shortCode);
            redisTemplate.opsForSet().add("dirty_urls", shortCode);
            return cached;
        }

        Instant now = Instant.now();
        Optional<Url> url = urlRepository.findByshortCode(shortCode);
        if(url.isPresent()) {
            Url u = url.get();
            if(now.isAfter(u.getExpiresAt())) {
                throw new RuntimeException("URL Expired !");
            }

            long ttl = u.getExpiresAt().getEpochSecond() - Instant.now().getEpochSecond();
            if(ttl>0) {
                redisTemplate.opsForValue().set(key, u.getOriginalUrl(), ttl, TimeUnit.SECONDS);
            }

            redisTemplate.opsForValue().increment("clicks:" + shortCode);
            return u.getOriginalUrl();
        }

        throw new RuntimeException("URL Not Found !");
    }


    public List<TopUrlResponse> getTop10Urls() {
        List<Url> urls = urlRepository.findTop10ByOrderByClickCountDesc();
        List<TopUrlResponse> result = new ArrayList<>();

        for(Url u:urls) {
            result.add(new TopUrlResponse(u.getOriginalUrl(),u.getShortCode(),u.getClickCount()));
        }

        return result;
    }
}
