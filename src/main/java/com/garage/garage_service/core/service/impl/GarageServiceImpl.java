package com.garage.garage_service.core.service.impl;

import com.garage.garage_service.core.constant.ApplicationMessages;
import com.garage.garage_service.core.exception.ModuleException;
import com.garage.garage_service.core.mapper.MapStructMapper;
import com.garage.garage_service.core.model.Garage;
import com.garage.garage_service.core.payload.common.ResponseEntityDto;
import com.garage.garage_service.core.payload.request.CreateGarageRequestDto;
import com.garage.garage_service.core.payload.response.GarageResponseDto;
import com.garage.garage_service.core.repository.dao.GarageDao;
import com.garage.garage_service.core.service.GarageService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class GarageServiceImpl implements GarageService {

	@NonNull
	private final GarageDao garageDao;

	@NonNull
	private final MessageSource messageSource;

	@NonNull
	private final MapStructMapper mapStructMapper;

	@Override
	@Transactional
	public ResponseEntityDto createGarage(CreateGarageRequestDto createGarageRequestDto) {
		log.debug("GarageServiceImpl.createGarage(): execution started");

		List<Garage> garages = garageDao.findAll();
		if (!garages.isEmpty()) {
			throw new ModuleException(
					messageSource.getMessage(ApplicationMessages.ERROR_GARAGE_ALREADY_EXISTS, null, Locale.ENGLISH));
		}

		Garage garage = new Garage();
		garage.setGarageName(createGarageRequestDto.getGarageName());
		garage.setWebsite(createGarageRequestDto.getWebsite());
		garage.setPhoneNumber(createGarageRequestDto.getPhoneNumber());
		garage.setAddress(createGarageRequestDto.getAddress());
		garage.setSocialMedia(createGarageRequestDto.getSocialMedia());

		Garage savedGarage = garageDao.save(garage);

		GarageResponseDto garageResponseDto = mapStructMapper.garageToGarageResponseDto(savedGarage);

		log.debug("GarageServiceImpl.createGarage(): execution ended");
		return new ResponseEntityDto(true, garageResponseDto);
	}

	@Override
	public ResponseEntityDto getGarage() {
		log.debug("GarageServiceImpl.getGarage(): execution started");

		Optional<Garage> garages = garageDao.findAll().stream().findFirst();
		if (garages.isEmpty()) {
			throw new ModuleException(
					messageSource.getMessage(ApplicationMessages.ERROR_GARAGE_NOT_FOUND, null, Locale.ENGLISH));
		}
		Garage garage = garages.get();

		GarageResponseDto garageResponseDto = mapStructMapper.garageToGarageResponseDto(garage);

		log.debug("GarageServiceImpl.getGarage(): execution ended");
		return new ResponseEntityDto(true, garageResponseDto);
	}

}
