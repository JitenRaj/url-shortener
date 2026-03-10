package com.jitenraj.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class UrlRequest {
    @NotBlank(message = "URL cannot be empty")
    @URL(message = "Must be a valid URL")
    private String url;
}
