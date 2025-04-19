package com.garage.garage_service.controller.v1;

import com.garage.garage_service.core.payload.common.ResponseEntityDto;
import com.garage.garage_service.core.payload.request.ServiceSlotRequestDto;
import com.garage.garage_service.core.service.ServiceSlotService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/garage/service/slot")
@RequiredArgsConstructor
public class ServiceSlotController {

	@NonNull
	private final ServiceSlotService serviceSlotService;

	@PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('SERVICE_MANAGER')")
	public ResponseEntity<ResponseEntityDto> createServiceSlot(
			@RequestBody ServiceSlotRequestDto serviceSlotRequestDto) {
		ResponseEntityDto response = serviceSlotService.createServiceSlot(serviceSlotRequestDto);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseEntityDto> getServiceSlots() {
		ResponseEntityDto response = serviceSlotService.getServiceSlots();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PatchMapping("/{slotId}")
	public ResponseEntity<ResponseEntityDto> updateServiceSlot(@PathVariable Long slotId,
			@RequestBody ServiceSlotRequestDto serviceSlotRequestDto) {
		ResponseEntityDto response = serviceSlotService.updateServiceSlot(slotId, serviceSlotRequestDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
