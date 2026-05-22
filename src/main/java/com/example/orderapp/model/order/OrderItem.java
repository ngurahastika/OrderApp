package com.example.orderapp.model.order;

import java.math.BigDecimal;

import com.example.orderapp.model.CreatorMod;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrderItem extends CreatorMod {

	@Id
	private Long id;

	private Long orderId;

	private Long productId;

	private Integer quantity;

	private BigDecimal price;
}
