package com.garage.garage_service.core.mapper;

import com.garage.garage_service.core.model.BookingRequest;
import com.garage.garage_service.core.model.Garage;
import com.garage.garage_service.core.model.ServiceSlot;
import com.garage.garage_service.core.payload.request.BookingRequestDto;
import com.garage.garage_service.core.payload.request.BookingRequestResponseDto;
import com.garage.garage_service.core.payload.request.ServiceSlotRequestDto;
import com.garage.garage_service.core.payload.response.GarageResponseDto;
import com.garage.garage_service.core.payload.response.ServiceSlotResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapStructMapper {

	GarageResponseDto garageToGarageResponseDto(Garage garage);

	ServiceSlot serviceSlotRequestDtoToServiceSlot(ServiceSlotRequestDto serviceSlotRequestDto);

	ServiceSlotResponseDto serviceSlotToServiceSlotResponseDto(ServiceSlot serviceSlot);

	List<ServiceSlotResponseDto> serviceSlotListToServiceSlotResponseDtoList(List<ServiceSlot> serviceSlots);

	BookingRequest bookingRequestDtoToBookingRequest(BookingRequestDto bookingRequestDto);

	BookingRequestResponseDto bookingRequestToBookingResponseDto(BookingRequest bookingRequest);

	List<BookingRequestResponseDto> bookingRequestListToBookingRequestResponseDtoList(
			List<BookingRequest> bookingRequests);

}
