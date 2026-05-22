package com.example.orderapp.service;

import org.springframework.amqp.AmqpException;

import com.example.orderapp.dto.BaseRes;
import com.example.orderapp.dto.CreateOrderRequest;
import com.example.orderapp.dto.ProsesOrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderService {

	public BaseRes createOrder(CreateOrderRequest request) throws Exception;

	public void prosesOrder(ProsesOrderDto order) throws JsonProcessingException, AmqpException;

	public BaseRes detail(Long id);

}
