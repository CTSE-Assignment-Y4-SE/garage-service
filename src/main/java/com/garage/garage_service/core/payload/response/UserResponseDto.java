package com.garage.garage_service.core.payload.response;

import com.garage.garage_service.core.type.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserResponseDto {

	private Long userId;

	private String email;

	private Role role;

}
