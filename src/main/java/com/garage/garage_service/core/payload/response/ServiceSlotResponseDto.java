package com.garage.garage_service.core.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Setter
@Getter
public class ServiceSlotResponseDto {

	private Long serviceSlotId;

	private LocalDate serviceDate;

	private LocalTime startTime;

	private LocalTime endTime;

	private int totalSlots;

	private int availableSlots;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}
