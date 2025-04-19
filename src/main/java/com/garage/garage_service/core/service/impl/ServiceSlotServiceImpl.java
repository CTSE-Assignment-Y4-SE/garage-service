package com.garage.garage_service.core.service.impl;

import com.garage.garage_service.core.constant.ApplicationMessages;
import com.garage.garage_service.core.constant.NotificationMessages;
import com.garage.garage_service.core.exception.ModuleException;
import com.garage.garage_service.core.mapper.MapStructMapper;
import com.garage.garage_service.core.model.ServiceSlot;
import com.garage.garage_service.core.payload.common.ResponseEntityDto;
import com.garage.garage_service.core.payload.request.NotificationRequestDto;
import com.garage.garage_service.core.payload.request.ServiceSlotRequestDto;
import com.garage.garage_service.core.payload.response.ServiceSlotResponseDto;
import com.garage.garage_service.core.payload.response.UserResponseDto;
import com.garage.garage_service.core.repository.dao.ServiceSlotDao;
import com.garage.garage_service.core.service.KafkaProducerService;
import com.garage.garage_service.core.service.NotificationService;
import com.garage.garage_service.core.service.ServiceSlotService;
import com.garage.garage_service.core.service.UserService;
import com.garage.garage_service.core.type.NotificationType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceSlotServiceImpl implements ServiceSlotService {

	@NonNull
	private final ServiceSlotDao serviceSlotDao;

	@NonNull
	private final MapStructMapper mapStructMapper;

	@NonNull
	private final MessageSource messageSource;

	@NonNull
	private final UserService userService;

	@NonNull
	private final NotificationService notificationService;

	@Override
	@Transactional
	public ResponseEntityDto createServiceSlot(ServiceSlotRequestDto serviceSlotRequestDto) {
		log.debug("ServiceSlotServiceImpl.createServiceSlot(): execution started");

		UserResponseDto currentUser = userService.getCurrentUser();

		serviceSlotRequestDto.normalize();

		validateServiceSlotRequestDtoTimeRange(serviceSlotRequestDto);

		boolean isOverlappingSlot = serviceSlotDao.existsByServiceDateAndTimeRange(
				serviceSlotRequestDto.getServiceDate(), serviceSlotRequestDto.getStartTime(),
				serviceSlotRequestDto.getEndTime());
		if (isOverlappingSlot) {
			throw new ModuleException(
					messageSource.getMessage(ApplicationMessages.ERROR_OVERLAPPING_SERVICE_SLOT, null, Locale.ENGLISH));
		}

		ServiceSlot serviceSlot = mapStructMapper.serviceSlotRequestDtoToServiceSlot(serviceSlotRequestDto);
		serviceSlot.setAvailableSlots(serviceSlotRequestDto.getSlots());
		serviceSlot.setTotalSlots(serviceSlotRequestDto.getSlots());

		ServiceSlot savedServiceSlot = serviceSlotDao.save(serviceSlot);

		NotificationRequestDto notificationRequestDto = getNotificationRequestDto(
				NotificationMessages.NOTIFICATION_SERVICE_SLOT_CREATED,
				NotificationMessages.NOTIFICATION_SERVICE_SLOT_CREATED, currentUser.getUserId());

		notificationService.sendNotificationAsync(notificationRequestDto);

		ServiceSlotResponseDto serviceSlotResponseDto = mapStructMapper
			.serviceSlotToServiceSlotResponseDto(savedServiceSlot);

		log.debug("ServiceSlotServiceImpl.createServiceSlot(): execution ended");
		return new ResponseEntityDto(true, serviceSlotResponseDto);
	}

	@Override
	public ResponseEntityDto getServiceSlots() {
		log.debug("ServiceSlotServiceImpl.getServiceSlots(): execution started");

		List<ServiceSlot> serviceSlots = serviceSlotDao.findAll();

		List<ServiceSlotResponseDto> serviceSlotResponseDtos = mapStructMapper
			.serviceSlotListToServiceSlotResponseDtoList(serviceSlots);

		log.debug("ServiceSlotServiceImpl.getServiceSlots(): execution ended");
		return new ResponseEntityDto(true, serviceSlotResponseDtos);
	}

	@Override
	@Transactional
	public ResponseEntityDto updateServiceSlot(Long slotId, ServiceSlotRequestDto serviceSlotRequestDto) {
		log.debug("ServiceSlotServiceImpl.updateServiceSlot(): execution started");

		UserResponseDto currentUser = userService.getCurrentUser();

		serviceSlotRequestDto.normalize();

		ServiceSlot existingServiceSlot = getServiceSlot(slotId);

		validateServiceSlotRequestDto(slotId, existingServiceSlot, serviceSlotRequestDto);

		if (serviceSlotRequestDto.getServiceDate() != null) {
			existingServiceSlot.setServiceDate(serviceSlotRequestDto.getServiceDate());
		}
		if (serviceSlotRequestDto.getStartTime() != null) {
			existingServiceSlot.setStartTime(serviceSlotRequestDto.getStartTime());
		}
		if (serviceSlotRequestDto.getEndTime() != null) {
			existingServiceSlot.setEndTime(serviceSlotRequestDto.getEndTime());
		}

		ServiceSlot savedServiceSlot = serviceSlotDao.save(existingServiceSlot);

		NotificationRequestDto notificationRequestDto = getNotificationRequestDto(
				NotificationMessages.NOTIFICATION_SERVICE_SLOT_UPDATED,
				NotificationMessages.NOTIFICATION_SERVICE_SLOT_UPDATED, currentUser.getUserId());

		notificationService.sendNotificationAsync(notificationRequestDto);

		ServiceSlotResponseDto serviceSlotResponseDto = mapStructMapper
			.serviceSlotToServiceSlotResponseDto(savedServiceSlot);

		log.debug("ServiceSlotServiceImpl.updateServiceSlot(): execution ended");
		return new ResponseEntityDto(true, serviceSlotResponseDto);
	}

	private void validateServiceSlotRequestDto(Long slotId, ServiceSlot existingServiceSlot,
			ServiceSlotRequestDto serviceSlotRequestDto) {

		validateServiceSlotRequestDtoTimeRange(serviceSlotRequestDto);

		if (serviceSlotRequestDto.getServiceDate() != null || serviceSlotRequestDto.getStartTime() != null
				|| serviceSlotRequestDto.getEndTime() != null) {
			boolean isOverlappingSlot = serviceSlotDao.existsByServiceDateAndTimeRangeExceptSlotId(
					serviceSlotRequestDto.getServiceDate() != null ? serviceSlotRequestDto.getServiceDate()
							: existingServiceSlot.getServiceDate(),
					serviceSlotRequestDto.getStartTime() != null ? serviceSlotRequestDto.getStartTime()
							: existingServiceSlot.getStartTime(),
					serviceSlotRequestDto.getEndTime() != null ? serviceSlotRequestDto.getEndTime()
							: existingServiceSlot.getEndTime(),
					slotId);
			if (isOverlappingSlot) {
				throw new ModuleException(messageSource.getMessage(ApplicationMessages.ERROR_OVERLAPPING_SERVICE_SLOT,
						null, Locale.ENGLISH));
			}
		}

		if (serviceSlotRequestDto.getSlots() != null) {
			int slotDifference = serviceSlotRequestDto.getSlots() - existingServiceSlot.getTotalSlots();
			if (existingServiceSlot.getAvailableSlots() + slotDifference < 0) {
				throw new ModuleException(messageSource.getMessage(ApplicationMessages.ERROR_INVALID_SERVICE_SLOT_COUNT,
						null, Locale.ENGLISH));
			}
			existingServiceSlot.setAvailableSlots(existingServiceSlot.getAvailableSlots() + slotDifference);
			existingServiceSlot.setTotalSlots(serviceSlotRequestDto.getSlots());
		}
	}

	private void validateServiceSlotRequestDtoTimeRange(ServiceSlotRequestDto serviceSlotRequestDto) {
		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now().withSecond(0).withNano(0);

		if (serviceSlotRequestDto.getStartTime() == null || serviceSlotRequestDto.getEndTime() == null) {
			throw new ModuleException(messageSource.getMessage(ApplicationMessages.ERROR_INVALID_SERVICE_SLOT_TIME,
					null, Locale.ENGLISH));
		}
		if (!serviceSlotRequestDto.getStartTime().isBefore(serviceSlotRequestDto.getEndTime())) {
			throw new ModuleException(messageSource.getMessage(ApplicationMessages.ERROR_INVALID_SERVICE_SLOT_TIME,
					null, Locale.ENGLISH));
		}

		if (serviceSlotRequestDto.getServiceDate() == null) {
			throw new ModuleException(messageSource.getMessage(ApplicationMessages.ERROR_INVALID_SERVICE_SLOT_DATE,
					null, Locale.ENGLISH));
		}
		if (serviceSlotRequestDto.getServiceDate().isBefore(today)) {
			throw new ModuleException(messageSource.getMessage(ApplicationMessages.ERROR_INVALID_SERVICE_SLOT_DATE,
					null, Locale.ENGLISH));
		}

		if (serviceSlotRequestDto.getServiceDate().isEqual(today)
				&& serviceSlotRequestDto.getStartTime().isBefore(now)) {
			throw new ModuleException(messageSource.getMessage(ApplicationMessages.ERROR_INVALID_SERVICE_SLOT_TIME,
					null, Locale.ENGLISH));
		}
	}

	private ServiceSlot getServiceSlot(Long slotId) {
		Optional<ServiceSlot> optionalServiceSlot = serviceSlotDao.findById(slotId);
		if (optionalServiceSlot.isEmpty()) {
			throw new ModuleException(
					messageSource.getMessage(ApplicationMessages.ERROR_SERVICE_SLOT_NOT_FOUND, null, Locale.ENGLISH));
		}
		return optionalServiceSlot.get();
	}

	private NotificationRequestDto getNotificationRequestDto(String title, String body, Long userId) {
		NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
		notificationRequestDto.setTitle(title);
		notificationRequestDto.setBody(body);
		notificationRequestDto.setNotificationType(NotificationType.SERVICE_SLOT);
		notificationRequestDto.setUserId(userId);
		notificationRequestDto.setBookingId(null);
		return notificationRequestDto;
	}

}
