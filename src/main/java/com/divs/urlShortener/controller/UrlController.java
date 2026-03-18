package com.divs.urlShortener.controller;

import org.springframework.web.bind.annotation.RestController;

import com.divs.urlShortener.service.UrlService;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }


    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> getOriginalUrl(@PathVariable String shortCode) {
        try {
            String url = urlService.getOriginalString(shortCode);

            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .location(URI.create(url))
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestParam String entity, @RequestParam Long ttl) {
        String code = urlService.createShortUrl(entity,ttl);
        return ResponseEntity.ok("http://localhost:8080/" + code);
    }
}
