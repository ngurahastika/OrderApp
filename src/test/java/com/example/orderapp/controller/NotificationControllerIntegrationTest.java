package com.example.orderapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class NotificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getNotificationSuccess() throws Exception {

        mockMvc.perform(get("/notifications").param("userId", "user-001"))
        
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value("00"));
    }
}