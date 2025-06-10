package com.teleport.exception.handler;

import com.teleport.controller.TrackingNumberController;
import com.teleport.service.TrackingNumberGeneratorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TrackingNumberController.class)
public class TrackingNumberControllerTestException {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrackingNumberGeneratorService generatorService;

    private final String baseUrl = "/tracker/next-tracking-number";

    @Test
    @DisplayName("Should return 400 for invalid origin_country_id")
    void testInvalidOriginParameter() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl)
                        .param("origin_country_id", "USX") // invalid (3 characters)
                        .param("destination_country_id", "IN")
                        .param("weight", "1.0")
                        .param("created_at", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                        .param("customer_id", "123")
                        .param("customer_name", "John")
                        .param("customer_slug", "teleport-inc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("generateTrackingNumber.origin_country_id: origin_country_id must be in ISO 3166-1 alpha-2 format"));
    }

    @Test
    @DisplayName("Should return 500 for tracking number generation failure")
    void testTrackingNumberGenerationFailure() throws Exception {
        Mockito.when(generatorService.generateTrackingNumber(anyString(), anyString(), anyString()))
                .thenThrow(new InvalidTrackingNumberException("Tracking number format invalid."));

        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl)
                        .param("origin_country_id", "US")
                        .param("destination_country_id", "IN")
                        .param("weight", "1.0")
                        .param("created_at", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                        .param("customer_id", "123")
                        .param("customer_name", "John")
                        .param("customer_slug", "teleport-inc"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Tracking number format invalid."))
                .andExpect(jsonPath("$.error").value("InvalidTrackingNumberException"));
    }

    @Test
    @DisplayName("Should return 400 for invalid customer_slug")
    void testInvalidCustomerSlugValidation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl)
                        .param("origin_country_id", "US")
                        .param("destination_country_id", "IN")
                        .param("weight", "1.0")
                        .param("created_at", OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                        .param("customer_id", "123")
                        .param("customer_name", "John")
                        .param("customer_slug", "Invalid_Slug"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("customer_slug must be in kebab-case"));
    }
}
