package com.divs.urlShortener.model;

import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "urls")
@Getter
@Setter
public class Url {
    @Id
    private ObjectId id;
    private String originalUrl;
    @Indexed(unique = true)
    private String shortCode;
    private Instant createdAt;
    @Indexed(expireAfterSeconds = 0)
    private Instant expiresAt;
    private Long clickCount;
}