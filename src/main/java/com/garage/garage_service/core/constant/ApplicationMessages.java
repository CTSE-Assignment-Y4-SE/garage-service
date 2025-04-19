package com.garage.garage_service.core.constant;

public final class ApplicationMessages {

	private ApplicationMessages() {
	}

	public static final String ERROR_DESERIALIZATION_FAILED = "api.error.serialization.failed";

	public static final String ERROR_SERIALIZATION_FAILED = "api.error.deserialization.failed";

	public static final String ERROR_GARAGE_ALREADY_EXISTS = "api.error.garage.already-exists";

	public static final String ERROR_GARAGE_NOT_FOUND = "api.error.garage.not-found";

	public static final String ERROR_OVERLAPPING_SERVICE_SLOT = "api.error.overlapping.service.slot";

	public static final String ERROR_ROLE_NOT_FOUND = "api.error.role.not-found";

	public static final String ERROR_SERVICE_SLOT_NOT_FOUND = "api.error.service.slot.not-found";

	public static final String ERROR_BOOKING_REQUEST_NOT_FOUND = "api.error.booking.request.not-found";

	public static final String ERROR_SERVICE_SLOT_COUNT_EXCEEDED = "api.error.service.slot.count-exceeded";

	public static final String ERROR_INVALID_SERVICE_SLOT_TIME = "api.error.invalid.service.slot.time";

	public static final String ERROR_INVALID_SERVICE_SLOT_DATE = "api.error.invalid.service.slot.date";

	public static final String ERROR_INVALID_SERVICE_SLOT_COUNT = "api.error.invalid.service.slot.count";

}
