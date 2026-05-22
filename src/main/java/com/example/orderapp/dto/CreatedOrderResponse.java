package com.example.orderapp.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.orderapp.enums.OrderStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreatedOrderResponse {

	@JsonSerialize(using = ToStringSerializer.class)
	private Long orderId;

	private OrderStatus status;
	private List<OrderItemRequest> items;
	private BigDecimal totalAmount;

	public CreatedOrderResponse(Long id, OrderStatus status, BigDecimal totalAmount) {
		this.orderId = id;
		this.status = status;
		this.totalAmount = totalAmount;

	}

}
