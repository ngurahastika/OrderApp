package com.example.orderapp.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.orderapp.config.RabbitMQConfig;
import com.example.orderapp.dto.NotifDto;
import com.example.orderapp.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationConsumer {

	private final NotificationService notificationService;
	private final ObjectMapper objectMapper;

	@RabbitListener(queues = RabbitMQConfig.ORDER_NOTIFICATION_QUEUE)
	public void consume(String data, Message message, Channel channel) throws IOException {
		try {

			NotifDto notif =  objectMapper.readValue(data, NotifDto.class);
			log.info("userId {}",notif.getUserId());
			this.notificationService.insertNotif(notif);

		} catch (Exception e) {
			log.error("error :", e);
			throw e;
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}
}