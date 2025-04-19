package com.garage.garage_service.core.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BookingRequestDto {

	private Long serviceSlotId;

	private Long vehicleId;

	private LocalDate bookingDate;

}
