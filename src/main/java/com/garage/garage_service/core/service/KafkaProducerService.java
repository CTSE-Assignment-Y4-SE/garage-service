package com.garage.garage_service.core.service;

import com.garage.garage_service.core.payload.request.NotificationRequestDto;

public interface KafkaProducerService {

	void sendNotification(NotificationRequestDto notificationRequestDto);

}
