package com.garage.garage_service.core.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class ServiceSlotRequestDto {

	@NotNull
	private LocalDate serviceDate;

	@NotNull
	private LocalTime startTime;

	@NotNull
	private LocalTime endTime;

	@NotNull
	@Positive
	private Integer slots;

	public void normalize() {
		if (startTime != null) {
			startTime = startTime.withSecond(0).withNano(0);
		}
		if (endTime != null) {
			endTime = endTime.withSecond(0).withNano(0);
		}
	}

}
