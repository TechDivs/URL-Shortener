package com.divs.urlShortener.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "urls")
public class Url {
    @Id
    private ObjectId id;
    private String originalUrl;
    @Indexed(unique = true)
    private String shortCode;
    private Instant createdAt;
    @Indexed(expireAfterSeconds = 0)
    private Instant expiresAt;
    @Indexed(direction = IndexDirection.DESCENDING)
    private Long clickCount;

    public ObjectId getId() {
        return id;
    }
    public void setId(ObjectId id) {
        this.id = id;
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
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
    public Instant getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
    public Long getClickCount() {
        return clickCount;
    }
    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }
}