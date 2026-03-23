<img width="1415" height="795" alt="image" src="https://github.com/user-attachments/assets/58e24618-0f9a-4e71-9719-5f7c2a17d596" />

# URL Shortener Backend

A scalable URL shortener built using **Spring Boot, Redis, and MongoDB**, designed with real-world system design principles like caching, rate limiting, and batched analytics.

---

## Features

### URL Shortening

* Converts long URLs into short, shareable links (~6–7 characters)
* Uses Base62 encoding on MongoDB ObjectId for uniqueness

### Fast Redirection

* Optimized lookup using Redis cache
* Fallback to MongoDB if cache miss

### Expiry (TTL)

* URLs expire after a configurable time (max 24 hours)
* MongoDB TTL index automatically removes expired entries

### Analytics

* Tracks number of clicks per short URL
* Uses Redis counters for high-performance tracking
* Periodic background job syncs data to MongoDB

### Top URLs

* `/top-urls` endpoint returns most visited URLs
* Optimized using indexed queries on `clickCount`

### Rate Limiting

* Implemented using Spring Interceptor + Redis
* Prevents abuse and protects backend:

  * `/shorten` → strict limit (e.g., 10 requests/min)
  * Redirect endpoints → higher limit (e.g., 200 requests/min)

---

## Architecture Overview

```text
Client
   ↓
Rate Limiter (Interceptor)
   ↓
Controller Layer
   ↓
Service Layer
   ↓
Redis (Cache + Counters)
   ↓
MongoDB (Persistent Storage)
```

---

## Request Flow

### 🔹 Create Short URL

```text
POST /shorten
   ↓
Generate shortCode
   ↓
Store in MongoDB
   ↓
Return short URL
```

---

### 🔹 Redirect Flow

```text
GET /{shortCode}
   ↓
Check Redis
   ↓
Cache hit → return URL
Cache miss → fetch from MongoDB → update Redis
   ↓
Increment click counter (Redis)
   ↓
Return redirect response
```

---

### 🔹 Analytics Sync (Background Job)

```text
Redis counters + dirty set
   ↓
Scheduler runs periodically
   ↓
Update MongoDB clickCount
   ↓
Clear Redis counters
```

---

## Tech Stack

* **Backend:** Spring Boot
* **Database:** MongoDB
* **Cache:** Redis
* **Language:** Java

---

## Key Design Concepts

* Read-through caching (Redis → MongoDB)
* Write-behind (batched analytics updates)
* TTL-based automatic data cleanup
* Rate limiting using middleware (Interceptor)
* Indexing for optimized queries
* Separation of concerns (Controller → Service → Repository)

---

## 🚀 API Endpoints

| Method | Endpoint       | Description                  |
| ------ | -------------- | ---------------------------- |
| POST   | `/shorten`     | Create short URL             |
| GET    | `/{shortCode}` | Redirect to original URL     |
| GET    | `/top-urls`    | Get top 10 most visited URLs |
| GET    | `/test`        | Health check                 |

---

## Future Improvements

* Custom short URLs
* Sliding window rate limiter
* Distributed deployment
* User-based rate limiting
* Real-time analytics dashboard

---

## Summary

This project demonstrates a production-style backend system with:

* High-performance caching
* Scalable analytics processing
* Clean architecture and middleware usage

---

### ⭐ If you found this useful, consider giving it a star!
