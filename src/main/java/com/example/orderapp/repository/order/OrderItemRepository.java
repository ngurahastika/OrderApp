package com.example.orderapp.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orderapp.model.order.OrderItem;
import java.util.List;


public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	
	List<OrderItem> findByOrderId(Long orderId);

}
