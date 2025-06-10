package com.teleport.service;

import com.teleport.exception.handler.InvalidTrackingNumberException;
import com.teleport.exception.handler.SequenceLimitExceededException;
import com.teleport.exception.handler.TrackingNumberGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TrackingNumberGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(TrackingNumberGeneratorService.class);
    private final RedisTemplate<String, Long> redisTemplate;

    public TrackingNumberGeneratorService(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String generateTrackingNumber(String origin, String destination, String slug) {
        try {
            String redisKey = String.format("track:%s:%s:%s", origin, destination, slug.toLowerCase());
            Long sequence = redisTemplate.opsForValue().increment(redisKey);
            redisTemplate.expire(redisKey, Duration.ofHours(24));

            if (sequence == null) {
                logger.error("Failed to increment Redis sequence for key: {}", redisKey);
                throw new TrackingNumberGenerationException("Unable to generate sequence number.");
            }

            if (sequence > 999999) {
                logger.warn("Sequence limit exceeded for key: {}", redisKey);
                throw new SequenceLimitExceededException("Sequence number exceeded limit for the day.");
            }

            String slugPrefix = slug.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
            slugPrefix = slugPrefix.length() >= 2 ? slugPrefix.substring(0, 2) : "XX";

            String prefix = String.format("%s%s%s", slugPrefix, origin.toUpperCase(), destination.toUpperCase());
            prefix = prefix.length() > 10 ? prefix.substring(0, 10) : prefix;

            String trackingNumber = String.format("%s%06d", prefix, sequence);

            if (!trackingNumber.matches("^[A-Z0-9]{1,16}$")) {
                logger.error("Generated tracking number [{}] does not match required pattern", trackingNumber);
                throw new InvalidTrackingNumberException("Tracking number format invalid.");
            }

            return trackingNumber;
        } catch (TrackingNumberGenerationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during tracking number generation", e);
            throw new TrackingNumberGenerationException("Unexpected error occurred.");
        }
    }


}