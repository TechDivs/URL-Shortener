package com.divs.urlShortener.dto;

public class TopUrlResponse {
    private String originalUrl;
    private String shortCode;
    private Long clickCount;

    public TopUrlResponse(String originalUrl, String shortCode, Long clickCount) {
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.clickCount = clickCount;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
    public String getShortCode() {
        return shortCode;
    }
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
    public Long getClickCount() {
        return clickCount;
    }
    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }
}
