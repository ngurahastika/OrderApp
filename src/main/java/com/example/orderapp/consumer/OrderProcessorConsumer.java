package com.example.orderapp.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.orderapp.config.RabbitMQConfig;
import com.example.orderapp.dto.ProsesOrderDto;
import com.example.orderapp.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderProcessorConsumer {

	private final OrderService orderService;
	private final ObjectMapper objectMapper;

	@RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
	public void consume(String data, Channel channel, Message message) throws IOException {
		try {

			ProsesOrderDto order =  objectMapper.readValue(data, ProsesOrderDto.class);
			log.info("userId {}",order.getUserId());
			this.orderService.prosesOrder(order);

		} catch (Exception e) {
			log.error("error :", e);
			throw e;
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}

	}
}