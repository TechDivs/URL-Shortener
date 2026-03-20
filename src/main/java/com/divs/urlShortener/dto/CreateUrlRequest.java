package com.divs.urlShortener.dto;


public class CreateUrlRequest {
    private String url;
    private Long ttl;
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Long getTtl() {
        return ttl;
    }
    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }
}