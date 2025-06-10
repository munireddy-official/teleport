package com.teleport.service;

import com.teleport.exception.handler.InvalidTrackingNumberException;
import com.teleport.exception.handler.SequenceLimitExceededException;
import com.teleport.exception.handler.TrackingNumberGenerationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
public class TrackingNumberGeneratorService {
    private static final Logger logger = LoggerFactory.getLogger(TrackingNumberGeneratorService.class);
    private static final int TRACKING_NUMBER_LENGTH = 16;
    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public String generateTrackingNumber(String origin, String destination, String slug) {
        String slugPrefix = slug.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        slugPrefix = slugPrefix.length() >= 2 ? slugPrefix.substring(0, 2) : "XX";

        String trackingNumber = getString(origin, destination, slugPrefix);

        if (trackingNumber.length() != TRACKING_NUMBER_LENGTH || !trackingNumber.matches("^[A-Z0-9]{" + TRACKING_NUMBER_LENGTH + "}$")) {
            logger.error("Generated tracking number [{}] does not match required length or pattern", trackingNumber);
            throw new InvalidTrackingNumberException("Internal error: Generated tracking number format invalid.");
        }

        return trackingNumber;
    }

    private static String getString(String origin, String destination, String slugPrefix) {
        String originCode = origin.toUpperCase();
        String destinationCode = destination.toUpperCase();

        String basePrefix = String.format("%s%s%s", slugPrefix, originCode.length() > 2 ? originCode.substring(0, 2) : originCode, destinationCode.length() > 2 ? destinationCode.substring(0, 2) : destinationCode);

        basePrefix = basePrefix.length() > 6 ? basePrefix.substring(0, 6) : basePrefix;

        Random random = new Random();
        StringBuilder randomSuffix = new StringBuilder();
        int remainingLength = TRACKING_NUMBER_LENGTH - basePrefix.length();

        for (int i = 0; i < remainingLength; i++) {
            randomSuffix.append(ALPHANUMERIC_CHARS.charAt(random.nextInt(ALPHANUMERIC_CHARS.length())));
        }
        return (basePrefix + randomSuffix).toUpperCase();
    }
}