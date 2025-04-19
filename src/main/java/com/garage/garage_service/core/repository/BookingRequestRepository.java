package com.garage.garage_service.core.repository;

import com.garage.garage_service.core.model.BookingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface BookingRequestRepository {

	Page<BookingRequest> findAllByStatusAndDate(String status, LocalDate bookingDate, Pageable pageable);

	Page<BookingRequest> findAllByStatusAndDateAndUserId(Long userId, String status, LocalDate bookingDate,
			Pageable pageable);

}
