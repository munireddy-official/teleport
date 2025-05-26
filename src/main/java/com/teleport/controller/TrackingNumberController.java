package com.teleport.controller;

import com.teleport.payload.TrackingResponse;
import com.teleport.util.TrackingNumberGenerator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/tracker")
public class TrackingNumberController {

    @GetMapping(value = "/next-tracking-number", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TrackingResponse generateTrackingNumber(
            @RequestParam String origin_country_id,
            @RequestParam String destination_country_id,
            @RequestParam double weight,
            @RequestParam(name = "created_at") String createdAtString,
            @RequestParam String customer_id,
            @RequestParam String customer_name,
            @RequestParam String customer_slug
    ) {
        OffsetDateTime createdAt;
        try {
            createdAt = OffsetDateTime.parse(createdAtString);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid created_at timestamp format (must be RFC 3339)");
        }
        String trackingNumber = TrackingNumberGenerator.generateTrackingNumber();
        return new TrackingResponse(trackingNumber, createdAt);
    }

}
