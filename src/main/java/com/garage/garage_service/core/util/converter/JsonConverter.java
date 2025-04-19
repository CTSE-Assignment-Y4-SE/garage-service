package com.garage.garage_service.core.util.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garage.garage_service.core.constant.ApplicationMessages;
import com.garage.garage_service.core.exception.ModuleException;
import com.garage.garage_service.core.type.SocialMediaPlatform;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.MessageSource;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

@Converter
@AllArgsConstructor
public class JsonConverter implements AttributeConverter<Map<SocialMediaPlatform, String>, String> {

	@NonNull
	private final MessageSource messageSource;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Map<SocialMediaPlatform, String> attribute) {
		if (attribute == null) {
			return null;
		}

		try {
			return objectMapper.writeValueAsString(attribute);
		}
		catch (JsonProcessingException e) {
			throw new ModuleException(
					messageSource.getMessage(ApplicationMessages.ERROR_DESERIALIZATION_FAILED, null, Locale.ENGLISH));
		}
	}

	@Override
	public Map<SocialMediaPlatform, String> convertToEntityAttribute(String databaseData) {
		if (databaseData == null) {
			return Collections.emptyMap();
		}

		try {
			return objectMapper.readValue(databaseData, new TypeReference<>() {
			});
		}
		catch (JsonProcessingException e) {
			throw new ModuleException(
					messageSource.getMessage(ApplicationMessages.ERROR_SERIALIZATION_FAILED, null, Locale.ENGLISH));
		}
	}

}
