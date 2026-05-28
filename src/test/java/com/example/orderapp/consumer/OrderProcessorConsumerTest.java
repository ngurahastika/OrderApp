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

import com.example.orderapp.dto.ProsesOrderDto;
import com.example.orderapp.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

@ExtendWith(MockitoExtension.class)
class OrderProcessorConsumerTest {

	@Mock
	private OrderService orderService;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private Channel channel;

	@InjectMocks
	private OrderProcessorConsumer consumer;

	@Test
	void consumeSuccess() throws Exception {

		String json = """
				{
				    "userId":"user-001",
				    "orderId":1001
				}
				""";

		ProsesOrderDto dto = new ProsesOrderDto("user-001", 1001L);

		MessageProperties props = new MessageProperties();

		props.setDeliveryTag(1L);

		Message message = new Message(new byte[] {}, props);

		when(objectMapper.readValue(json, ProsesOrderDto.class)).thenReturn(dto);

		doNothing().when(orderService).prosesOrder(any(ProsesOrderDto.class));

		consumer.consume(json, channel, message);

		verify(objectMapper).readValue(json, ProsesOrderDto.class);

		verify(orderService).prosesOrder(dto);

		verify(channel).basicAck(1L, false);
	}

	@Test
	void consumeFailedInvalidJson() throws Exception {

		String json = "invalid-json";

		MessageProperties props = new MessageProperties();

		props.setDeliveryTag(2L);

		Message message = new Message(new byte[] {}, props);

		when(objectMapper.readValue(json, ProsesOrderDto.class)).thenThrow(new RuntimeException("Invalid JSON"));

		assertThrows(RuntimeException.class, () -> consumer.consume(json, channel, message));

		verify(channel).basicAck(2L, false);
	}

	@Test
	void consumeFailedProcessOrder() throws Exception {

		String json = """
				{
				    "userId":"user-002",
				    "orderId":2001
				}
				""";

		ProsesOrderDto dto = new ProsesOrderDto("user-002", 2001L);

		MessageProperties props = new MessageProperties();

		props.setDeliveryTag(3L);

		Message message = new Message(new byte[] {}, props);

		when(objectMapper.readValue(json, ProsesOrderDto.class)).thenReturn(dto);

		doNothing().when(channel).basicAck(3L, false);

		doNothing().when(orderService).prosesOrder(any(ProsesOrderDto.class));

		consumer.consume(json, channel, message);

		verify(orderService).prosesOrder(dto);

		verify(channel).basicAck(3L, false);
	}
}
