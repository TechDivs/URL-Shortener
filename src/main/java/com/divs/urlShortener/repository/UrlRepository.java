package com.divs.urlShortener.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.divs.urlShortener.model.Url;


public interface UrlRepository extends MongoRepository<Url,ObjectId>{
    Optional<Url> findByshortCode(String shortCode);
}
