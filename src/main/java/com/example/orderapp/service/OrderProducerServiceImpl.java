package com.example.orderapp.service;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.orderapp.config.RabbitMQConfig;
import com.example.orderapp.dto.NotifDto;
import com.example.orderapp.dto.ProsesOrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProducerServiceImpl implements OrderProducerService {

	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;

	public void publishCreated(ProsesOrderDto order) throws JsonProcessingException, AmqpException {

		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "order.created", objectMapper.writeValueAsString(order));
	}

	public void publishPaid(NotifDto notif) throws JsonProcessingException, AmqpException {
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "order.paid", objectMapper.writeValueAsString(notif));
	}

	public void publishFailed(NotifDto notif) throws JsonProcessingException, AmqpException {
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "order.failed", objectMapper.writeValueAsString(notif));
	}
}