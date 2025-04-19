package com.garage.garage_service.core.service;

import com.garage.garage_service.core.payload.common.ResponseEntityDto;
import com.garage.garage_service.core.payload.request.CreateGarageRequestDto;

public interface GarageService {

	ResponseEntityDto createGarage(CreateGarageRequestDto createGarageRequestDto);

	ResponseEntityDto getGarage();

}
