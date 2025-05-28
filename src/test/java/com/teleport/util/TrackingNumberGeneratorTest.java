package com.teleport.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrackingNumberGeneratorTest {
    @Test
    void shouldGenerate16CharAlphanumericTrackingNumber() {
        String trackingNumber = TrackingNumberGenerator.generateTrackingNumber();
        assertNotNull(trackingNumber);
        assertEquals(16, trackingNumber.length());
        assertTrue(trackingNumber.matches("^[A-Z0-9]{16}$"));
    }

    @Test
    void shouldGenerateUniqueTrackingNumbers() {
        String one = TrackingNumberGenerator.generateTrackingNumber();
        String two = TrackingNumberGenerator.generateTrackingNumber();
        assertNotEquals(one, two);
    }
}
