package com.example.orderapp.consumer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import com.example.orderapp.dto.NotifDto;
import com.example.orderapp.enums.OrderStatus;
import com.example.orderapp.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

	@Mock
	private NotificationService notificationService;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private Channel channel;

	@InjectMocks
	private NotificationConsumer notificationConsumer;

	@Test
	void consumeSuccess() throws Exception {

		String json = """
				{
				    "idOrder":1,
				    "userId":"user-001",
				    "orderStatus":"PAID"
				}
				""";

		NotifDto dto = new NotifDto(1L, "user-001", OrderStatus.PAID);

		MessageProperties props = new MessageProperties();

		props.setDeliveryTag(1L);

		Message message = new Message(new byte[] {}, props);

		when(objectMapper.readValue(json, NotifDto.class)).thenReturn(dto);

		doNothing().when(notificationService).insertNotif(any(NotifDto.class));

		notificationConsumer.consume(json, message, channel);

		verify(objectMapper).readValue(json, NotifDto.class);

		verify(notificationService).insertNotif(dto);

		verify(channel).basicAck(1L, false);
	}

	@Test
	void consumeFailedInvalidJson() throws Exception {

		String json = "invalid-json";

		MessageProperties props = new MessageProperties();

		props.setDeliveryTag(10L);

		Message message = new Message(new byte[] {}, props);

		when(objectMapper.readValue(json, NotifDto.class)).thenThrow(new RuntimeException("Invalid JSON"));

		assertThrows(RuntimeException.class, () -> notificationConsumer.consume(json, message, channel));

		verify(channel).basicAck(10L, false);
	}

	@Test
	void consumeFailedInsertNotif() throws Exception {

		String json = """
				{
				    "idOrder":2,
				    "userId":"user-002",
				    "orderStatus":"FAILED"
				}
				""";

		NotifDto dto = new NotifDto(2L, "user-002", OrderStatus.FAILED);

		MessageProperties props = new MessageProperties();

		props.setDeliveryTag(20L);

		Message message = new Message(new byte[] {}, props);

		when(objectMapper.readValue(json, NotifDto.class)).thenReturn(dto);

		doNothing().when(notificationService).insertNotif(any(NotifDto.class));

		doNothing().when(channel).basicAck(20L, false);

		notificationConsumer.consume(json, message, channel);

		verify(notificationService).insertNotif(dto);

		verify(channel).basicAck(20L, false);
	}
}
