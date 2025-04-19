package com.garage.garage_service.core.grpc.client;

import com.garage.garage_service.core.grpc.server.TokenValidateServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceClientConfig {

	@Value("${grpc.address.name}")
	private String grpcAddressName;

	@Value("${grpc.address.service-port.user}")
	private int grpcUserServicePort;

	@Bean
	public TokenValidateServiceGrpc.TokenValidateServiceBlockingStub tokenValidateServiceBlockingStub() {
		return TokenValidateServiceGrpc.newBlockingStub(
				ManagedChannelBuilder.forAddress(grpcAddressName, grpcUserServicePort).usePlaintext().build());
	}

}
