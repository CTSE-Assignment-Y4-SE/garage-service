package com.garage.garage_service.core.service.impl;

import com.garage.garage_service.core.constant.ApplicationMessages;
import com.garage.garage_service.core.constant.NotificationMessages;
import com.garage.garage_service.core.exception.ModuleException;
import com.garage.garage_service.core.mapper.MapStructMapper;
import com.garage.garage_service.core.model.BookingRequest;
import com.garage.garage_service.core.model.ServiceSlot;
import com.garage.garage_service.core.payload.common.PageDto;
import com.garage.garage_service.core.payload.common.ResponseEntityDto;
import com.garage.garage_service.core.payload.request.BookingRequestDto;
import com.garage.garage_service.core.payload.request.BookingRequestFilterDto;
import com.garage.garage_service.core.payload.request.BookingRequestResponseDto;
import com.garage.garage_service.core.payload.request.BookingRequestStatusUpdateRequestDto;
import com.garage.garage_service.core.payload.request.NotificationRequestDto;
import com.garage.garage_service.core.payload.response.UserResponseDto;
import com.garage.garage_service.core.repository.dao.BookingRequestDao;
import com.garage.garage_service.core.repository.dao.ServiceSlotDao;
import com.garage.garage_service.core.service.BookingRequestService;
import com.garage.garage_service.core.service.KafkaProducerService;
import com.garage.garage_service.core.service.NotificationService;
import com.garage.garage_service.core.service.UserService;
import com.garage.garage_service.core.type.BookingRequestStatus;
import com.garage.garage_service.core.type.NotificationType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Service
@AllArgsConstructor
@Slf4j
public class BookingRequestServiceImpl implements BookingRequestService {

	@NonNull
	private final BookingRequestDao bookingRequestDao;

	@NonNull
	private final MapStructMapper mapStructMapper;

	@NonNull
	private final UserService userService;

	@NonNull
	private final MessageSource messageSource;

	@NonNull
	private final ServiceSlotDao serviceSlotDao;

	@NonNull
	private final NotificationService notificationService;

	@Override
	@Transactional
	public ResponseEntityDto createBookingRequest(BookingRequestDto bookingRequestDto) {
		log.debug("BookingRequestServiceImpl.createBookingRequest(): execution started");

		UserResponseDto currentUser = userService.getCurrentUser();

		Optional<ServiceSlot> optionalServiceSlot = serviceSlotDao.findById(bookingRequestDto.getServiceSlotId());
		if (optionalServiceSlot.isEmpty()) {
			throw new ModuleException(
					messageSource.getMessage(ApplicationMessages.ERROR_SERVICE_SLOT_NOT_FOUND, null, Locale.ENGLISH));
		}
		ServiceSlot serviceSlot = optionalServiceSlot.get();

		if (serviceSlot.getAvailableSlots() == 0) {
			throw new ModuleException(messageSource.getMessage(ApplicationMessages.ERROR_SERVICE_SLOT_COUNT_EXCEEDED,
					null, Locale.ENGLISH));
		}

		BookingRequest bookingRequest = mapStructMapper.bookingRequestDtoToBookingRequest(bookingRequestDto);
		bookingRequest.setServiceSlot(serviceSlot);
		bookingRequest.setUserId(currentUser.getUserId());
		bookingRequest.setStatus(BookingRequestStatus.PENDING);

		serviceSlot.setAvailableSlots(serviceSlot.getAvailableSlots() - 1);
		serviceSlotDao.save(serviceSlot);

		BookingRequest savedBookingRequest = bookingRequestDao.save(bookingRequest);

		BookingRequestResponseDto bookingRequestResponseDto = mapStructMapper
			.bookingRequestToBookingResponseDto(savedBookingRequest);

		NotificationRequestDto notificationRequestDto = getNotificationRequestDto(
				NotificationMessages.NOTIFICATION_BOOKING_REQUEST_CREATED,
				NotificationMessages.NOTIFICATION_BOOKING_REQUEST_CREATED, currentUser.getUserId(),
				savedBookingRequest.getBookingRequestId());

		notificationService.sendNotificationAsync(notificationRequestDto);

		log.debug("BookingRequestServiceImpl.createBookingRequest(): execution ended");
		return new ResponseEntityDto(true, bookingRequestResponseDto);
	}

	@Override
	public ResponseEntityDto getAllBookingRequests(BookingRequestFilterDto bookingRequestFilterDto) {
		log.debug("BookingRequestServiceImpl.getAllBookingRequests(): execution started");

		List<BookingRequest> bookingRequests;

		if (bookingRequestFilterDto.isExport()) {
			bookingRequests = bookingRequestDao.findAllByStatusAndDateNoPagination(
					bookingRequestFilterDto.getStatus() != null ? bookingRequestFilterDto.getStatus().name() : null,
					bookingRequestFilterDto.getDate());
			List<BookingRequestResponseDto> bookingRequestResponseDtos = mapStructMapper
				.bookingRequestListToBookingRequestResponseDtoList(bookingRequests);

			log.debug("BookingRequestServiceImpl.getAllBookingRequests(): execution ended");
			return new ResponseEntityDto(true, bookingRequestResponseDtos);
		}

		Pageable pageable = PageRequest.of(bookingRequestFilterDto.getOffset() - 1, bookingRequestFilterDto.getLimit());
		Page<BookingRequest> bookingRequestPage = bookingRequestDao.findAllByStatusAndDate(
				bookingRequestFilterDto.getStatus() != null ? bookingRequestFilterDto.getStatus().name() : null,
				bookingRequestFilterDto.getDate(), pageable);
		bookingRequests = bookingRequestPage.getContent();

		List<BookingRequestResponseDto> bookingRequestResponseDtos = mapStructMapper
			.bookingRequestListToBookingRequestResponseDtoList(bookingRequests);

		PageDto pageDto = new PageDto();
		pageDto.setItems(bookingRequestResponseDtos);
		pageDto.setTotalItems(bookingRequestPage.getTotalElements());
		pageDto.setCurrentPage(bookingRequestFilterDto.getOffset());
		pageDto.setTotalPages(bookingRequestPage.getTotalPages());

		log.debug("BookingRequestServiceImpl.getAllBookingRequests(): execution ended");
		return new ResponseEntityDto(true, pageDto);
	}

	@Override
	public ResponseEntityDto getMyBookingRequests(BookingRequestFilterDto bookingRequestFilterDto) {
		log.debug("BookingRequestServiceImpl.getMyBookingRequests(): execution started");

		UserResponseDto currentUser = userService.getCurrentUser();

		Pageable pageable = PageRequest.of(bookingRequestFilterDto.getOffset() - 1, bookingRequestFilterDto.getLimit());

		Page<BookingRequest> bookingRequestPage = bookingRequestDao.findAllByStatusAndDateAndUserId(
				currentUser.getUserId(),
				bookingRequestFilterDto.getStatus() != null ? bookingRequestFilterDto.getStatus().name() : null,
				bookingRequestFilterDto.getDate(), pageable);

		List<BookingRequestResponseDto> bookingRequestResponseDtos = mapStructMapper
			.bookingRequestListToBookingRequestResponseDtoList(bookingRequestPage.getContent());

		PageDto pageDto = new PageDto();
		pageDto.setItems(bookingRequestResponseDtos);
		pageDto.setTotalItems(bookingRequestPage.getTotalElements());
		pageDto.setCurrentPage(bookingRequestFilterDto.getOffset());
		pageDto.setTotalPages(bookingRequestPage.getTotalPages());

		log.debug("BookingRequestServiceImpl.getMyBookingRequests(): execution ended");
		return new ResponseEntityDto(true, pageDto);
	}

	@Override
	@Transactional
	public ResponseEntityDto updateBookingRequestStatus(Long bookingRequestId,
			BookingRequestStatusUpdateRequestDto bookingRequestStatusUpdateRequestDto) {
		log.debug("BookingRequestServiceImpl.updateBookingRequestStatus(): execution started");

		Optional<BookingRequest> optionalBookingRequest = bookingRequestDao.findById(bookingRequestId);
		if (optionalBookingRequest.isEmpty()) {
			throw new ModuleException(messageSource.getMessage(ApplicationMessages.ERROR_BOOKING_REQUEST_NOT_FOUND,
					null, Locale.ENGLISH));
		}
		BookingRequest bookingRequest = optionalBookingRequest.get();

		bookingRequest.setStatus(bookingRequestStatusUpdateRequestDto.getStatus());

		ServiceSlot serviceSlot = bookingRequest.getServiceSlot();
		if (bookingRequestStatusUpdateRequestDto.getStatus() == BookingRequestStatus.REJECTED) {
			serviceSlot.setAvailableSlots(serviceSlot.getAvailableSlots() + 1);
			serviceSlotDao.save(serviceSlot);
		}

		bookingRequestDao.save(bookingRequest);

		NotificationRequestDto notificationRequestDto = getNotificationRequestDto(null, null,
				bookingRequest.getUserId(), bookingRequest.getBookingRequestId());

		switch (bookingRequestStatusUpdateRequestDto.getStatus()) {
			case BookingRequestStatus.CONFIRMED -> {
				notificationRequestDto.setTitle(NotificationMessages.NOTIFICATION_BOOKING_REQUEST_CONFIRMED);
				notificationRequestDto.setBody(NotificationMessages.NOTIFICATION_BOOKING_REQUEST_CONFIRMED);
			}
			case BookingRequestStatus.REJECTED -> {
				notificationRequestDto.setTitle(NotificationMessages.NOTIFICATION_BOOKING_REQUEST_REJECTED);
				notificationRequestDto.setBody(NotificationMessages.NOTIFICATION_BOOKING_REQUEST_REJECTED);
			}
			case BookingRequestStatus.CANCELLED -> {
				notificationRequestDto.setTitle(NotificationMessages.NOTIFICATION_BOOKING_REQUEST_CANCELLED);
				notificationRequestDto.setBody(NotificationMessages.NOTIFICATION_BOOKING_REQUEST_CANCELLED);
			}
			default ->
				log.error("BookingRequestServiceImpl.updateBookingRequestStatus(): unknown booking request status");
		}

		notificationService.sendNotificationAsync(notificationRequestDto);

		log.debug("BookingRequestServiceImpl.updateBookingRequestStatus(): execution ended");
		return new ResponseEntityDto(true, null);
	}

	private NotificationRequestDto getNotificationRequestDto(String title, String body, Long userId,
			Long bookingRequestId) {
		NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
		notificationRequestDto.setTitle(title);
		notificationRequestDto.setBody(body);
		notificationRequestDto.setNotificationType(NotificationType.BOOKING_REQUEST);
		notificationRequestDto.setUserId(userId);
		notificationRequestDto.setBookingId(bookingRequestId);
		return notificationRequestDto;
	}

}
