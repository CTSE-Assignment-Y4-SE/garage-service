package com.garage.garage_service.core.service;

import com.garage.garage_service.core.payload.common.ResponseEntityDto;
import com.garage.garage_service.core.payload.request.ServiceSlotRequestDto;

public interface ServiceSlotService {

	ResponseEntityDto createServiceSlot(ServiceSlotRequestDto serviceSlotRequestDto);

	ResponseEntityDto getServiceSlots();

	ResponseEntityDto updateServiceSlot(Long slotId, ServiceSlotRequestDto serviceSlotRequestDto);

}
