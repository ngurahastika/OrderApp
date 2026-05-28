package com.example.orderapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.orderapp.constants.CommonConstant;
import com.example.orderapp.dto.BaseRes;
import com.example.orderapp.dto.NotifDto;
import com.example.orderapp.dto.NotificationResponse;
import com.example.orderapp.dto.Pagination;
import com.example.orderapp.enums.OrderStatus;
import com.example.orderapp.model.order.Notification;
import com.example.orderapp.repository.order.NotificationRepository;
import com.example.orderapp.utils.DateUtils;
import com.example.orderapp.utils.SnowflakeIdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final SnowflakeIdGenerator snowflakeIdGenerator;

	public void insertNotif(NotifDto notifDto) {

		String text;

		if (notifDto.getOrderStatus() == OrderStatus.PAID) {
			text = "Order #" + notifDto.getIdOrder() + " berhasil dibayar";
		} else {
			text = "Order #" + notifDto.getIdOrder() + " gagal diproses";
		}
		Notification notif = new Notification();
		notif.setId(snowflakeIdGenerator.nextId());
		notif.setIsRead(false);
		notif.setMessage(text);
		notif.setUserId(notifDto.getUserId());
		notif.setCreatedBy(notifDto.getUserId());
		notif.setCreatedDt(DateUtils.getCurrentSQLTimestamp());
		this.notificationRepository.save(notif);
	}

	public BaseRes<List<NotificationResponse>> listingNotif(String userId, Pageable pageable) {
		BaseRes<List<NotificationResponse>> response = new BaseRes<List<NotificationResponse>>();
		try {

			Page<Notification> page = this.notificationRepository.findByUserId(userId, pageable);

			List<NotificationResponse> listNotif = page.getContent().stream().map(p -> {
				NotificationResponse nr = new NotificationResponse();
				nr.setRead(p.getIsRead());
				nr.setMessage(p.getMessage());
				nr.setCreatedDt(p.getCreatedDt());
				return nr;
			}).toList();

			Pagination pagination = new Pagination();
			pagination.setMaxRow(page.getSize());
			pagination.setPage(page.getNumber());
			pagination.setTotalPage(page.getTotalPages());
			pagination.setTotalRecord(page.getNumberOfElements());

			response.setData(listNotif);
			response.setPagination(pagination);
			response.setStatusCode(CommonConstant.STATUS_CODE_SUCCESS);
			response.setStatusDesc(CommonConstant.STATUS_DESC_SUCCESS);
		} catch (Exception e) {
			log.error("error :", e);
			throw e;
		}
		return response;
	}

}
