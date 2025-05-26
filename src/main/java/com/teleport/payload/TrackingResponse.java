package com.teleport.payload;

import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class TrackingResponse {
    private final String trackingNumber;
    private final OffsetDateTime createdAt;

    public TrackingResponse(String trackingNumber, OffsetDateTime createdAt) {
        this.trackingNumber = trackingNumber;
        this.createdAt = createdAt;
    }

}
