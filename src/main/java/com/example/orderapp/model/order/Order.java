package com.example.orderapp.model.order;

import java.math.BigDecimal;

import com.example.orderapp.enums.OrderStatus;
import com.example.orderapp.model.CreatorMod;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Order extends CreatorMod {

	@Id
	private Long id;

	private String userId;

	private BigDecimal totalAmount;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

}
