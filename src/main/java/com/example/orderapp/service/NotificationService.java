package com.example.orderapp.service;

import org.springframework.data.domain.Pageable;

import com.example.orderapp.dto.BaseRes;
import com.example.orderapp.dto.NotifDto;

public interface NotificationService {

	public void insertNotif(NotifDto notifDto);

	public BaseRes listingNotif(String userId, Pageable pageable);

}
