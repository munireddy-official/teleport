package com.teleport.controller;

import com.teleport.payload.TrackingResponse;
import com.teleport.util.TrackingNumberGenerator;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/tracker")
@Validated
public class TrackingNumberController {

    @GetMapping(value = "/next-tracking-number", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateTrackingNumber(
            @RequestParam @NotBlank @Pattern(regexp = "^[A-Z]{2}$", message = "origin_country_id must be in ISO 3166-1 alpha-2 format") String origin_country_id,
            @RequestParam @NotBlank @Pattern(regexp = "^[A-Z]{2}$", message = "destination_country_id must be in ISO 3166-1 alpha-2 format") String destination_country_id,
            @RequestParam @NotNull @DecimalMin(value = "0.001", inclusive = true, message = "Weight must be positive") @Digits(integer = 10, fraction = 3, message = "Weight must have up to 3 decimal places") double weight,
            @RequestParam(name = "created_at") String createdAtString,
            @RequestParam String customer_id,
            @RequestParam String customer_name,
            @RequestParam @NotBlank @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "customer_slug must be in kebab-case") String customer_slug
    ) {
        OffsetDateTime createdAt;
        try {
            createdAt = OffsetDateTime.parse(createdAtString);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid created_at timestamp format (must be RFC 3339)");
        }
        String trackingNumber = TrackingNumberGenerator.generateTrackingNumber();
        TrackingResponse response = new TrackingResponse();
        response.setCreatedAt(createdAt);
        response.setTrackingNumber(trackingNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
