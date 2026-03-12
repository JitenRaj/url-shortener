package com.jitenraj.urlshortener.controller;

import com.jitenraj.urlshortener.dto.UrlRequest;
import com.jitenraj.urlshortener.dto.UrlResponse;
import com.jitenraj.urlshortener.service.UrlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/shorten")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public ResponseEntity<UrlResponse> createShortUrl(@Valid @RequestBody UrlRequest request) {
        return new ResponseEntity<>(urlService.createShortUrl(request), HttpStatus.CREATED);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        UrlResponse response = urlService.getOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(response.getUrl()))
                .build();
    }

    @GetMapping("/{shortCode}/stats")
    public ResponseEntity<UrlResponse> getUrlStats(@PathVariable String shortCode) {
        return ResponseEntity.ok(urlService.getUrlStats(shortCode));
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable String shortCode) {
        urlService.deleteShortUrl(shortCode);
        return ResponseEntity.noContent().build();
    }
}