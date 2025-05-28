package com.teleport.payload;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class TrackingRequest {
    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}$", message = "origin_country_id must be in ISO 3166-1 alpha-2 format")
    private String origin_country_id;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}$", message = "destination_country_id must be in ISO 3166-1 alpha-2 format")
    private String destination_country_id;

    @NotNull
    @DecimalMin(value = "0.001", inclusive = true, message = "Weight must be positive")
    @Digits(integer = 10, fraction = 3, message = "Weight must have up to 3 decimal places")
    private BigDecimal weight;

    @NotNull
    private OffsetDateTime created_at;

    @NotNull
    private UUID customer_id;

    @NotBlank
    private String customer_name;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "customer_slug must be in kebab-case")
    private String customer_slug;

}
