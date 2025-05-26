package com.teleport.util;

import java.util.UUID;

public class TrackingNumberGenerator {
    public static String generateTrackingNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}
