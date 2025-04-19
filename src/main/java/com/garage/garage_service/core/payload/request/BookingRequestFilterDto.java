package com.garage.garage_service.core.payload.request;

import com.garage.garage_service.core.type.BookingRequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BookingRequestFilterDto {

	private BookingRequestStatus status;

	private LocalDate date;

	private int offset = 1;

	private int limit = 10;

	private boolean isExport = false;

}
