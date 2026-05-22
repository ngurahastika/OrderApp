package com.example.orderapp.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.orderapp.enums.OrderStatus;
import com.example.orderapp.model.order.Order;
import com.example.orderapp.model.order.OrderItem;
import com.example.orderapp.repository.order.OrderItemRepository;
import com.example.orderapp.repository.order.OrderRepository;
import com.example.orderapp.repository.order.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataOrderServiceImpl implements DataOrderService {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveOrder(Order order, List<OrderItem> orderItems) {
		this.orderRepository.save(order);
		this.orderItemRepository.saveAll(orderItems);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int updateStock(Long productId, Integer qty) {
		return this.productRepository.deductStock(productId, qty);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int updateStatusOrder(Long id, OrderStatus status) {
		return this.orderRepository.updateStatus(id, status);
	}

}
