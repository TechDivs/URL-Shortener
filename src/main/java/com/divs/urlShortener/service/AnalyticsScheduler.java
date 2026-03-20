package com.divs.urlShortener.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.divs.urlShortener.model.Url;
import com.divs.urlShortener.repository.UrlRepository;

@Service
public class AnalyticsScheduler {
    private final RedisTemplate<String, String> redisTemplate;
    private final UrlRepository urlRepository;

    public AnalyticsScheduler(RedisTemplate<String, String> redisTemplate, UrlRepository urlRepository) {
        this.redisTemplate = redisTemplate;
        this.urlRepository = urlRepository;
    }

    @Scheduled(fixedDelay = 60000)
    public void syncClicks() {
        Set<String> dirtyUrls = redisTemplate.opsForSet().members("dirty_urls");
        if (dirtyUrls == null || dirtyUrls.isEmpty()) return;

        for(String shortCode : dirtyUrls) {
            String key = "clicks:" + shortCode;
            String countStr = redisTemplate.opsForValue().get(key);

            if(countStr==null) continue;
            long count = Long.parseLong(countStr);
            Optional<Url> urlOpt = urlRepository.findByshortCode(shortCode);

            if(urlOpt.isPresent()) {
                Url url = urlOpt.get();
                url.setClickCount(url.getClickCount() + count);
                urlRepository.save(url);
            }
            
            redisTemplate.delete(key);
            redisTemplate.opsForSet().remove("dirty_urls", shortCode);
        }
    }
}
