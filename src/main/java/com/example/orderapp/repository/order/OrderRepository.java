package com.example.orderapp.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.orderapp.enums.OrderStatus;
import com.example.orderapp.model.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	
	@Modifying
	@Query(value = "UPDATE Order SET status = :status WHERE id = :id ")
	public int updateStatus(Long id, OrderStatus status);

}
