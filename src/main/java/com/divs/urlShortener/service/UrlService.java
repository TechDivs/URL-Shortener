package com.divs.urlShortener.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.divs.urlShortener.model.Url;
import com.divs.urlShortener.repository.UrlRepository;


@Service
public class UrlService {
    private UrlRepository urlRepository;
    
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository=urlRepository;
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

        String shortCode = encodeBase62(url.getId().toHexString());
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
}
