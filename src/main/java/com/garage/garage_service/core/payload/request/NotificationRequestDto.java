package com.garage.garage_service.core.payload.request;

import com.garage.garage_service.core.type.NotificationType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationRequestDto {

	private String title;

	private String body;

	private NotificationType notificationType;

	private Long userId;

	private Long bookingId;

}
