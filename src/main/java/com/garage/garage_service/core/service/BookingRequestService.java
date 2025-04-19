package com.garage.garage_service.core.service;

import com.garage.garage_service.core.payload.common.ResponseEntityDto;
import com.garage.garage_service.core.payload.request.BookingRequestDto;
import com.garage.garage_service.core.payload.request.BookingRequestFilterDto;
import com.garage.garage_service.core.payload.request.BookingRequestStatusUpdateRequestDto;

public interface BookingRequestService {

	ResponseEntityDto createBookingRequest(BookingRequestDto bookingRequestDto);

	ResponseEntityDto getAllBookingRequests(BookingRequestFilterDto bookingRequestFilterDto);

	ResponseEntityDto getMyBookingRequests(BookingRequestFilterDto bookingRequestFilterDto);

	ResponseEntityDto updateBookingRequestStatus(Long bookingRequestId,
			BookingRequestStatusUpdateRequestDto bookingRequestStatusUpdateRequestDto);

}
