package com.example.orderapp.dto;

import com.example.orderapp.enums.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotifDto {

	private Long idOrder;
	private String userId;
	private OrderStatus orderStatus;

}
