package com.divs.urlShortener.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUrlRequest {
    private String url;
    private Long ttl;
}