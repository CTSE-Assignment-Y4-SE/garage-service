package com.garage.garage_service.core.service.impl;

import com.garage.garage_service.core.constant.ApplicationMessages;
import com.garage.garage_service.core.exception.ModuleException;
import com.garage.garage_service.core.grpc.server.TokenValidateRequest;
import com.garage.garage_service.core.grpc.server.TokenValidateResponse;
import com.garage.garage_service.core.grpc.server.TokenValidateServiceGrpc;
import com.garage.garage_service.core.payload.response.UserResponseDto;
import com.garage.garage_service.core.service.UserService;
import com.garage.garage_service.core.type.Role;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	@NonNull
	private final TokenValidateServiceGrpc.TokenValidateServiceBlockingStub tokenValidateServiceBlockingStub;

	@NonNull
	private final MessageSource messageSource;

	@Override
	public TokenValidateResponse isTokenValid(@NonNull String token) {
		TokenValidateRequest request = TokenValidateRequest.newBuilder().setToken(token).build();
		return tokenValidateServiceBlockingStub.validateToken(request);
	}

	@Override
	public UserResponseDto getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();

			String userId = null;
			if (authentication.getDetails() instanceof Map<?, ?> details && details.containsKey("claims")) {
				Object claimsObject = details.get("claims");

				if (claimsObject instanceof Map<?, ?> claimsMap) {
					Object userIdObject = claimsMap.get("userId");
					if (userIdObject instanceof String userIdString) {
						userId = userIdString;
					}
				}
			}

			String authority = authentication.getAuthorities()
				.stream()
				.map(GrantedAuthority::getAuthority)
				.findFirst()
				.orElseThrow(() -> new ModuleException(
						messageSource.getMessage(ApplicationMessages.ERROR_ROLE_NOT_FOUND, null, Locale.ENGLISH)));

			String simplifiedRole = authority.replaceFirst("^ROLE_", "");

			UserResponseDto userResponseDto = new UserResponseDto();
			assert userId != null;
			userResponseDto.setUserId(Long.valueOf(userId));
			userResponseDto.setEmail(username);
			userResponseDto.setRole(Role.valueOf(simplifiedRole));

			return userResponseDto;
		}

		return null;
	}

}
