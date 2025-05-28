package com.teleport.payload;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrackingResponseTest {
    @Test
    void shouldStoreTrackingNumberAndCreatedAt() {
        String trackingNumber = "ABC123DEF456GHI7";
        OffsetDateTime createdAt = OffsetDateTime.now();
        TrackingResponse response = new TrackingResponse();
        response.setTrackingNumber(trackingNumber);
        response.setCreatedAt(createdAt.toString());

        assertEquals(trackingNumber, response.getTrackingNumber());
        assertEquals(createdAt, response.getCreatedAt());
    }
}
