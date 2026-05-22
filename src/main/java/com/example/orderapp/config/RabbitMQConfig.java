package com.example.orderapp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String EXCHANGE = "order.ex";

	public static final String ORDER_CREATED_QUEUE = "order.created.q";
	public static final String ORDER_NOTIFICATION_QUEUE = "order.notification.q";

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(EXCHANGE);
	}

	@Bean
	Queue orderCreatedQueue() {
		return new Queue(ORDER_CREATED_QUEUE);
	}

	@Bean
	Queue notificationQueue() {
		return new Queue(ORDER_NOTIFICATION_QUEUE);
	}

	@Bean
	Binding createdBinding() {
		return BindingBuilder.bind(orderCreatedQueue()).to(exchange()).with("order.created");
	}

	@Bean
	Binding paidBinding() {
		return BindingBuilder.bind(notificationQueue()).to(exchange()).with("order.paid");
	}

	@Bean
	Binding failedBinding() {
		return BindingBuilder.bind(notificationQueue()).to(exchange()).with("order.failed");
	}
}