package com.teleport.exception.handler;

import com.teleport.controller.TrackingNumberController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackingNumberController.class)
public class ValidationExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenInvalidOriginCountryId_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tracker/next-tracking-number")
                        .param("origin_country_id", "IND")
                        .param("destination_country_id", "MY")
                        .param("weight", "1.23")
                        .param("created_at", "2025-05-27T10:00:00+05:30")
                        .param("customer_id", "de619854-b59b-425e-9db4-943979e1bd49")
                        .param("customer_name", "RedBox Logistics")
                        .param("customer_slug", "redbox-logistics")
                        .header("Accept", "application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Validation failed")));
    }

    @Test
    void whenMissingRequiredParam_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tracker/next-tracking-number")
                        .param("origin_country_id", "IN")
                        .param("destination_country_id", "MYN")
                        .param("weight", "1.23")
                        .param("created_at", "2025-05-27T10:00:00+05:30")
                        .param("customer_id", "de619854-b59b-425e-9db4-943979e1bd49")
                        .param("customer_name", "RedBox Logistics")
                        .param("customer_slug", "redbox-logistics")
                        .header("Accept", "application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Validation failed")));
    }
}
