package com.garage.garage_service.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garage.garage_service.core.payload.request.NotificationRequestDto;
import com.garage.garage_service.core.service.KafkaProducerService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

	@NonNull
	private final KafkaTemplate<String, String> kafkaTemplate;

	@NonNull
	private final ObjectMapper objectMapper;

	@Override
	public void sendNotification(NotificationRequestDto notificationRequestDto) {
		log.debug("KafkaProducerServiceImpl.sendNotification(): execution started");

		try {
			String notificationRequestJson = objectMapper.writeValueAsString(notificationRequestDto);
			kafkaTemplate.send("garage-notifications", notificationRequestJson);
		}
		catch (JsonProcessingException e) {
			log.error("kafkaProducerServiceImpl.sendNotification(): execution failed- {}", e.getMessage());
		}

		log.debug("KafkaProducerServiceImpl.sendNotification(): execution ended");
	}

}
