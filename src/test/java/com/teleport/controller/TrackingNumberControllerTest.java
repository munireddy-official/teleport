package com.teleport.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackingNumberController.class)
public class TrackingNumberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnTrackingNumberAndCreatedAt() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tracker/next-tracking-number")
                        .param("origin_country_id", "MY")
                        .param("destination_country_id", "ID")
                        .param("weight", "1.234")
                        .param("created_at", "2025-05-26T14:30:00+05:30")
                        .param("customer_id", "de619854-b59b-425e-9db4-943979e1bd49")
                        .param("customer_name", "RedBox Logistics")
                        .param("customer_slug", "redbox-logistics")
                        .header("Accept", "application/json"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.trackingNumber").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").isString());
    }
}
