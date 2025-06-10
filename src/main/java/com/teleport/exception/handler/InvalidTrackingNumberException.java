package com.teleport.exception.handler;

public class InvalidTrackingNumberException extends TrackingNumberGenerationException {
    public InvalidTrackingNumberException(String message) {
        super(message);
    }
}
