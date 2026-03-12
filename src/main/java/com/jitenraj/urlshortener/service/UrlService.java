package com.jitenraj.urlshortener.service;

import com.jitenraj.urlshortener.dto.UrlRequest;
import com.jitenraj.urlshortener.dto.UrlResponse;
import com.jitenraj.urlshortener.entity.UrlEntity;
import com.jitenraj.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_CODE_LENGTH = 7;
    private final SecureRandom random = new SecureRandom();

    public UrlResponse createShortUrl(UrlRequest request) {
        String shortCode;
        do {
            shortCode = generateRandomCode();
        } while (urlRepository.existsByShortCode(shortCode));

        UrlEntity entity = UrlEntity.builder()
                .url(request.getUrl())
                .shortCode(shortCode)
                .accessCount(0)
                .build();

        UrlEntity savedEntity = urlRepository.save(entity);
        return mapToResponse(savedEntity);
    }

    public UrlResponse getOriginalUrl(String shortCode) {
        UrlEntity entity = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));

        entity.setAccessCount(entity.getAccessCount() + 1);
        urlRepository.save(entity);

        return mapToResponse(entity);
    }

    public void deleteShortUrl(String shortCode) {
        UrlEntity entity = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));
        urlRepository.delete(entity);
    }

    public UrlResponse getUrlStats(String shortCode) {
        UrlEntity entity = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Short URL not found"));
        return mapToResponse(entity);
    }

    private String generateRandomCode() {
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    private UrlResponse mapToResponse(UrlEntity entity) {
        UrlResponse response = new UrlResponse();
        response.setId(entity.getId());
        response.setUrl(entity.getUrl());
        response.setShortCode(entity.getShortCode());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        response.setAccessCount(entity.getAccessCount());
        return response;
    }
}
