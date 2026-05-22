package com.example.orderapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderapp.dto.CreateOrderRequest;
import com.example.orderapp.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<Object> createOrder(@Valid @RequestBody CreateOrderRequest request) throws Exception {
		return ResponseEntity.status(HttpStatus.CREATED).body(this.orderService.createOrder(request));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getProductsDetail(@PathVariable(name = "id") Long id) {
		return ResponseEntity.ok(orderService.detail(id));
	}

}
