package com.example.orderapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	private static String ORDER_ID;

	@Test
	@Order(1)
	void createOrderSuccess() throws Exception {
		String response = mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "userId": "user-001",
				  "items": [
				    {
				      "productId": 1,
				      "quantity": 1
				    }
				  ]
				}
				""")).andExpect(status().isCreated()).andExpect(jsonPath("$.statusCode").value("00"))
				.andExpect(jsonPath("$.data.orderId").exists()).andReturn().getResponse().getContentAsString();

		ORDER_ID = com.jayway.jsonpath.JsonPath.read(response, "$.data.orderId").toString();
	}

	@Test
	@Order(2)
	void createOrderValidateStock() throws Exception {

		mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content("""
				{
				  "userId": "user-001",
				  "items": [
				    {
				      "productId": 1,
				      "quantity": 999999999
				    }
				  ]
				}
				""")).andExpect(status().is4xxClientError())
				.andExpect(jsonPath("$.message").value("Stock tidak cukup"));

	}

	@Test
	@Order(3)
	void detailOrderSuccess() throws Exception {
		mockMvc.perform(get("/orders/" + ORDER_ID)).andExpect(status().isOk())
				.andExpect(jsonPath("$.statusCode").value("00")).andExpect(jsonPath("$.data.orderId").exists());
	}
}