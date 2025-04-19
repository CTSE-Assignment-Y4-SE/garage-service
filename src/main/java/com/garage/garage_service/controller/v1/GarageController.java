package com.garage.garage_service.controller.v1;

import com.garage.garage_service.core.payload.common.ResponseEntityDto;
import com.garage.garage_service.core.payload.request.CreateGarageRequestDto;
import com.garage.garage_service.core.service.GarageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/garage")
@RequiredArgsConstructor
public class GarageController {

	@NonNull
	private final GarageService garageService;

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseEntityDto> createGarage(@RequestBody CreateGarageRequestDto createGarageRequestDto) {
		ResponseEntityDto response = garageService.createGarage(createGarageRequestDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseEntityDto> getGarage() {
		ResponseEntityDto response = garageService.getGarage();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
