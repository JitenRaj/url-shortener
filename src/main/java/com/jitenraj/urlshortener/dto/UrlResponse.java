package com.jitenraj.urlshortener.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlResponse {
    private Long id;
    private String url;
    private String shortCode;
    private LocalDateTime createdAt;
    private Integer accessCount;
}
