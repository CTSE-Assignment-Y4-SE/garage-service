package com.garage.garage_service.core.payload.request;

import com.garage.garage_service.core.payload.response.ServiceSlotResponseDto;
import com.garage.garage_service.core.type.BookingRequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class BookingRequestResponseDto {

	private Long bookingRequestId;

	private Long userId;

	private ServiceSlotResponseDto serviceSlot;

	private Long vehicleId;

	private LocalDate bookingDate;

	private BookingRequestStatus status;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}
