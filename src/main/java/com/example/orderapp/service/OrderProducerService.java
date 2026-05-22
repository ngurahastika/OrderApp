package com.example.orderapp.service;

import org.springframework.amqp.AmqpException;

import com.example.orderapp.dto.NotifDto;
import com.example.orderapp.dto.ProsesOrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OrderProducerService {

	public void publishCreated(ProsesOrderDto order) throws JsonProcessingException, AmqpException;

	public void publishPaid(NotifDto notif) throws JsonProcessingException, AmqpException;

	public void publishFailed(NotifDto notif) throws JsonProcessingException, AmqpException;
}
