package com.garage.garage_service.core.payload.response;

import com.garage.garage_service.core.type.SocialMediaPlatform;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class GarageResponseDto {

	private Long garageId;

	private String garageName;

	private String website;

	private String phoneNumber;

	private String address;

	private Map<SocialMediaPlatform, String> socialMedia;

}
