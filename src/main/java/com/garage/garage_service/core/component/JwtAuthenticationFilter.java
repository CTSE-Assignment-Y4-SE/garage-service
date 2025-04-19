package com.garage.garage_service.core.component;

import com.garage.garage_service.core.grpc.server.TokenValidateResponse;
import com.garage.garage_service.core.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@NonNull
	private final UserService userService;

	private static final List<String> WHITELISTED_ENDPOINTS = List.of("/v1/garage");

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			TokenValidateResponse tokenValidateResponse = userService.isTokenValid(token);

			if (tokenValidateResponse != null && tokenValidateResponse.getIsValid()) {
				Map<String, String> claims = tokenValidateResponse.getClaimsMap();
				String username = claims.get("sub");
				List<String> roles = List.of(claims.get("role"));

				List<SimpleGrantedAuthority> authorities = roles.stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
					.toList();

				Map<String, Object> details = Map.of("claims", claims);

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,
						token, authorities);
				authentication.setDetails(details);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
		// String requestURI = request.getRequestURI();
		// return WHITELISTED_ENDPOINTS.stream().anyMatch(requestURI::startsWith);
		return false;
	}

}
