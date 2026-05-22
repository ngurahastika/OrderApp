package com.example.orderapp.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.orderapp.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	@GetMapping
	public ResponseEntity<Object> getNotifications(@RequestParam String userId,
			@PageableDefault(page = 0, size = 10, sort = "createdDt", direction = Sort.Direction.DESC) Pageable pageable) {

		return ResponseEntity.ok(notificationService.listingNotif(userId, pageable));
	}
}