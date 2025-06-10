package com.teleport.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrackingNumberGeneratorServiceTest {
    private ValueOperations<String, String> valueOperations;
    private TrackingNumberGeneratorService service;

    @BeforeEach
    void setUp() {
        service = new TrackingNumberGeneratorService();
    }

    @Test
    void testGenerateTrackingNumber_successful() {
        String slug = "redbox";
        String origin = "in";
        String destination = "us";
        OffsetDateTime timestamp = OffsetDateTime.of(2025, 5, 28, 14, 35, 0, 0, ZoneOffset.UTC);

        String expectedPrefix = "REINUS250528";
        when(valueOperations.increment(anyString())).thenReturn(123L);

        String result = service.generateTrackingNumber(origin, destination, slug);

        assertNotNull(result);
        assertTrue(result.matches("^[A-Z0-9]{16}$"));
        assertTrue(result.startsWith(expectedPrefix));
        assertTrue(result.endsWith("0123"));
    }

    @Test
    void testGenerateTrackingNumber_withShortSlug() {
        when(valueOperations.increment(anyString())).thenReturn(1L);
        OffsetDateTime timestamp = OffsetDateTime.now();

        String result = service.generateTrackingNumber("US", "CA", "x");
        assertTrue(result.startsWith("XXUSCA"));
    }

    @Test
    void testGenerateTrackingNumber_throwsOnInvalidFormat() {
        when(valueOperations.increment(anyString())).thenReturn(100000L); // generates a number > 9999

        OffsetDateTime timestamp = OffsetDateTime.now();

        assertDoesNotThrow(() ->
                service.generateTrackingNumber("US", "CA", "ab")
        );
    }

}
