package com.example.orderapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.orderapp.dto.BaseRes;
import com.example.orderapp.dto.NotifDto;
import com.example.orderapp.dto.NotificationResponse;
import com.example.orderapp.dto.Pagination;
import com.example.orderapp.enums.OrderStatus;
import com.example.orderapp.model.order.Notification;
import com.example.orderapp.repository.order.NotificationRepository;
import com.example.orderapp.utils.SnowflakeIdGenerator;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

	@Mock
	private NotificationRepository notificationRepository;

	@Mock
	private SnowflakeIdGenerator snowflakeIdGenerator;

	@InjectMocks
	private NotificationServiceImpl notificationService;

	@Test
	void insertNotifPaidSuccess() {

		when(snowflakeIdGenerator.nextId()).thenReturn(1001L);

		NotifDto dto = new NotifDto(1L, "user-001", OrderStatus.PAID);

		notificationService.insertNotif(dto);

		ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);

		verify(notificationRepository).save(captor.capture());

		Notification notif = captor.getValue();

		assertEquals(1001L, notif.getId());

		assertEquals("user-001", notif.getUserId());

		assertEquals("Order #1 berhasil dibayar", notif.getMessage());

		assertFalse(notif.getIsRead());

		assertNotNull(notif.getCreatedDt());
	}

	@Test
	void insertNotifFailedSuccess() {

		when(snowflakeIdGenerator.nextId()).thenReturn(2001L);

		NotifDto dto = new NotifDto(2L, "user-001", OrderStatus.FAILED);

		notificationService.insertNotif(dto);

		ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);

		verify(notificationRepository).save(captor.capture());

		Notification notif = captor.getValue();

		assertEquals("Order #2 gagal diproses", notif.getMessage());

		assertEquals("user-001", notif.getUserId());
	}

	@Test
	void listingNotifSuccess() {

		Notification notif1 = new Notification();

		notif1.setId(1L);
		notif1.setUserId("user-001");
		notif1.setMessage("Notif 1");
		notif1.setIsRead(false);
		notif1.setCreatedDt(Timestamp.valueOf(LocalDateTime.now()));

		Notification notif2 = new Notification();

		notif2.setId(2L);
		notif2.setUserId("user-001");
		notif2.setMessage("Notif 2");
		notif2.setIsRead(true);
		notif2.setCreatedDt(Timestamp.valueOf(LocalDateTime.now()));

		List<Notification> listNotif = List.of(notif1, notif2);

		Pageable pageable = PageRequest.of(0, 10);

		Page<Notification> page = new PageImpl<>(listNotif, pageable, 2);

		when(notificationRepository.findByUserId("user-001", pageable)).thenReturn(page);

		BaseRes<List<NotificationResponse>> response = notificationService.listingNotif("user-001", pageable);

		assertEquals("00", response.getStatusCode());

		assertNotNull(response.getData());


		assertEquals("Notif 1", response.getData().get(0).getMessage());

		assertEquals(false, response.getData().get(0).isRead());

		Pagination pagination = response.getPagination();

		assertEquals(0, pagination.getPage());

		assertEquals(10, pagination.getMaxRow());

		assertEquals(1, pagination.getTotalPage());

		assertEquals(2, pagination.getTotalRecord());

		verify(notificationRepository).findByUserId("user-001", pageable);
	}

	@Test
	void listingNotifEmptySuccess() {

		Pageable pageable = PageRequest.of(0, 10);

		Page<Notification> page = new PageImpl<>(List.of(), pageable, 0);

		when(notificationRepository.findByUserId("user-001", pageable)).thenReturn(page);

		BaseRes<List<NotificationResponse>> response = notificationService.listingNotif("user-001", pageable);

		assertEquals("00", response.getStatusCode());

		

		assertEquals(0, response.getData().size());
	}
}
