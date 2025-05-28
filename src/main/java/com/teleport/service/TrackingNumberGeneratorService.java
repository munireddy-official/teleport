package com.teleport.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TrackingNumberGeneratorService {

    private final RedisTemplate<String, Long> redisTemplate;

    public TrackingNumberGeneratorService(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateTrackingNumber(String origin, String destination, String slug, OffsetDateTime timestamp) {
        // Normalize and validate inputs
        String originCode = origin.trim().toUpperCase();
        String destinationCode = destination.trim().toUpperCase();

        String slugPrefix = slug.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        slugPrefix = (slugPrefix.length() >= 2) ? slugPrefix.substring(0, 2) : String.format("%-2s", slugPrefix).replace(' ', 'X');

        // Use date only to partition Redis keys (e.g., yyMMdd)
        String datePart = timestamp.format(DateTimeFormatter.ofPattern("yyMMdd"));

        // Compose Redis key for the counter
        String redisKey = String.format("track:%s:%s:%s:%s", slugPrefix, originCode, destinationCode, datePart);

        // Atomic counter from Redis
        Long sequence = redisTemplate.opsForValue().increment(redisKey);
        redisTemplate.expire(redisKey, Duration.ofDays(1)); // optional cleanup

        String counter = String.format("%04d", sequence % 10000); // 4-digit sequence (wraps at 9999)

        // Final tracking number format
        String trackingNumber = slugPrefix + originCode + destinationCode + datePart + counter;

        // Ensure length and pattern
        if (trackingNumber.length() > 16) {
            throw new IllegalStateException("Generated tracking number exceeds 16 characters: " + trackingNumber);
        }
        if (!trackingNumber.matches("^[A-Z0-9]{1,16}$")) {
            throw new IllegalStateException("Generated tracking number does not match required format: " + trackingNumber);
        }

        return trackingNumber;
    }

}