package com.example.orderapp.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponse {

	private String message;
	private Timestamp createdDt;
	private boolean isRead;

}
