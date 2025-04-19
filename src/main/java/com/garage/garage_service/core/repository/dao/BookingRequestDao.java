package com.garage.garage_service.core.repository.dao;

import com.garage.garage_service.core.model.BookingRequest;
import com.garage.garage_service.core.repository.BookingRequestRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRequestDao extends JpaRepository<BookingRequest, Long>,
		JpaSpecificationExecutor<BookingRequest>, BookingRequestRepository {

	@Query(value = """
			SELECT * FROM booking_request
			WHERE (:status IS NULL OR status = :status)
			AND (:bookingDate IS NULL OR booking_date = :bookingDate)
			ORDER BY booking_date DESC
			""", nativeQuery = true)
	List<BookingRequest> findAllByStatusAndDateNoPagination(String status, LocalDate bookingDate);

}
