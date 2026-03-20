package com.divs.urlShortener.controller;

import org.springframework.web.bind.annotation.RestController;

import com.divs.urlShortener.dto.CreateUrlRequest;
import com.divs.urlShortener.service.UrlService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }
    

    @GetMapping("/test")
    public String test() {
        return "WORKING";
    }


    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody CreateUrlRequest request) {
        long ttl = request.getTtl() == null ? 86400 : request.getTtl();
        String code = urlService.createShortUrl(request.getUrl(),ttl);
        return ResponseEntity.ok("http://localhost:8080/" + code);
    }


    @GetMapping("/resolve/{shortCode}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortCode) {
        try {
            String url = urlService.getOriginalString(shortCode);
            return ResponseEntity.ok(url);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
