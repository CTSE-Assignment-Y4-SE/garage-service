package com.garage.garage_service.core.repository.impl;

import com.garage.garage_service.core.model.BookingRequest;
import com.garage.garage_service.core.model.BookingRequest_;
import com.garage.garage_service.core.repository.BookingRequestRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookingRequestRepositoryImpl implements BookingRequestRepository {

	@NonNull
	private final EntityManager entityManager;

	@Override
	public Page<BookingRequest> findAllByStatusAndDate(String status, LocalDate bookingDate, Pageable pageable) {
		return findAllByCriteria(null, status, bookingDate, pageable);
	}

	@Override
	public Page<BookingRequest> findAllByStatusAndDateAndUserId(Long userId, String status, LocalDate bookingDate,
			Pageable pageable) {
		return findAllByCriteria(userId, status, bookingDate, pageable);
	}

	private Page<BookingRequest> findAllByCriteria(Long userId, String status, LocalDate bookingDate,
			Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<BookingRequest> criteriaQuery = criteriaBuilder.createQuery(BookingRequest.class);
		Root<BookingRequest> root = criteriaQuery.from(BookingRequest.class);

		List<Predicate> predicates = buildPredicates(criteriaBuilder, root, userId, status, bookingDate);

		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(criteriaBuilder.asc(root.get(BookingRequest_.STATUS)));

		TypedQuery<BookingRequest> query = entityManager.createQuery(criteriaQuery);
		int totalRows = query.getResultList().size();

		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		query.setMaxResults(pageable.getPageSize());

		return new PageImpl<>(query.getResultList(), pageable, totalRows);
	}

	private List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<BookingRequest> root, Long userId,
			String status, LocalDate bookingDate) {
		List<Predicate> predicates = new ArrayList<>();

		if (userId != null) {
			predicates.add(criteriaBuilder.equal(root.get(BookingRequest_.USER_ID), userId));
		}

		if (status != null) {
			predicates.add(criteriaBuilder.equal(root.get(BookingRequest_.STATUS), status));
		}

		if (bookingDate != null) {
			predicates.add(criteriaBuilder.equal(root.get(BookingRequest_.BOOKING_DATE), bookingDate));
		}

		return predicates;
	}

}
