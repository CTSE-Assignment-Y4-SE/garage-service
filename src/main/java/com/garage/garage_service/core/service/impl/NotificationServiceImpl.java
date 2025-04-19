package com.garage.garage_service.core.service.impl;

import com.garage.garage_service.core.payload.request.NotificationRequestDto;
import com.garage.garage_service.core.service.KafkaProducerService;
import com.garage.garage_service.core.service.NotificationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.N;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

	@NonNull
	private final KafkaProducerService kafkaProducerService;

	@NonNull
	private final ExecutorService executorService;

	@Override
	public void sendNotificationAsync(NotificationRequestDto notificationRequestDto) {
		log.info("NotificationServiceImpl.sendNotificationAsync(): execution started");

		executorService.execute(() -> {
			try {
				kafkaProducerService.sendNotification(notificationRequestDto);
				log.info("NotificationServiceImpl.sendNotificationAsync(): notification sent successfully");
			}
			catch (Exception e) {
				log.error("NotificationServiceImpl.sendNotificationAsync(): failed to send notification", e);
			}
		});

		log.info("NotificationServiceImpl.sendNotificationAsync(): execution ended");
	}

}
