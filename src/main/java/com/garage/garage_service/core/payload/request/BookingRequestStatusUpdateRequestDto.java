package com.garage.garage_service.core.payload.request;

import com.garage.garage_service.core.type.BookingRequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingRequestStatusUpdateRequestDto {

	@NotNull
	private BookingRequestStatus status;

}
