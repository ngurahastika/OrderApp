package com.example.orderapp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemRequest {

	@NotNull(message = "productId mandatory")
	private Long productId;

	@Min(value = 1, message = "quantity minimum 1")
	@NotNull(message = "quantity mandatory")
	private Integer quantity;
}