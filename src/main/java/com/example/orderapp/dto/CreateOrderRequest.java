package com.example.orderapp.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {

	@NotBlank(message = "UserId mandatory")
	private String userId;

	@Valid
	@Size(min = 1, message = "items minimum 1")
	private List<OrderItemRequest> items;
}