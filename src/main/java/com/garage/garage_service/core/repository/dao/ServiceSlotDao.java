package com.garage.garage_service.core.repository.dao;

import com.garage.garage_service.core.model.ServiceSlot;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ServiceSlotDao extends JpaRepository<ServiceSlot, Long> {

	@Query(value = """
				SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
				FROM service_slot s
				WHERE s.service_date = :serviceDate
				AND (s.start_time < :endTime AND s.end_time > :startTime)
			""", nativeQuery = true)
	boolean existsByServiceDateAndTimeRange(@NotNull LocalDate serviceDate, @NotNull LocalTime startTime,
			@NotNull LocalTime endTime);

	@Query(value = """
				SELECT COUNT(*) > 0 FROM service_slot
				WHERE service_date = :serviceDate
				AND (:startTime < end_time AND :endTime > start_time)
				AND slot_id != :slotId
			""", nativeQuery = true)
	boolean existsByServiceDateAndTimeRangeExceptSlotId(LocalDate serviceDate, LocalTime startTime, LocalTime endTime,
			Long slotId);

}
