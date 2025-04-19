package com.garage.garage_service.controller.v1;

import com.garage.garage_service.core.payload.common.ResponseEntityDto;
import com.garage.garage_service.core.payload.request.BookingRequestDto;
import com.garage.garage_service.core.payload.request.BookingRequestFilterDto;
import com.garage.garage_service.core.payload.request.BookingRequestStatusUpdateRequestDto;
import com.garage.garage_service.core.service.BookingRequestService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/garage/booking/request")
@RequiredArgsConstructor
public class BookingRequestController {

	@NonNull
	private final BookingRequestService bookingRequestService;

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseEntityDto> createBookingRequest(@RequestBody BookingRequestDto bookingRequestDto) {
		ResponseEntityDto response = bookingRequestService.createBookingRequest(bookingRequestDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAnyRole('SERVICE_MANAGER', 'GARAGE_ADMIN')")
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseEntityDto> getBookingRequests(BookingRequestFilterDto bookingRequestFilterDto) {
		ResponseEntityDto response = bookingRequestService.getAllBookingRequests(bookingRequestFilterDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('VEHICLE_OWNER')")
	@GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseEntityDto> getMyBookingRequests(BookingRequestFilterDto bookingRequestFilterDto) {
		ResponseEntityDto response = bookingRequestService.getMyBookingRequests(bookingRequestFilterDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasRole('SERVICE_MANAGER')")
	@PatchMapping(value = "/{bookingRequestId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseEntityDto> updateBookingRequestStatus(@PathVariable Long bookingRequestId,
			@RequestBody BookingRequestStatusUpdateRequestDto bookingRequestStatusUpdateRequestDto) {
		ResponseEntityDto response = bookingRequestService.updateBookingRequestStatus(bookingRequestId,
				bookingRequestStatusUpdateRequestDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
