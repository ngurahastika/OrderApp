package com.example.orderapp.service;

import java.util.List;

import com.example.orderapp.enums.OrderStatus;
import com.example.orderapp.model.order.Order;
import com.example.orderapp.model.order.OrderItem;

public interface DataOrderService {

	public void saveOrder(Order order, List<OrderItem> orderItems);
	public int updateStock(Long productId, Integer qty);
	public int updateStatusOrder(Long id, OrderStatus status);

}
