package com.garage.garage_service.core.model;

import com.garage.garage_service.core.type.SocialMediaPlatform;
import com.garage.garage_service.core.util.converter.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "garage")
@Setter
@Getter
public class Garage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "garage_id")
	private Long garageId;

	@Column(name = "garage_name", length = 255, nullable = false)
	private String garageName;

	@Column(name = "website", length = 255)
	private String website;

	@Column(name = "phone_number", length = 30)
	private String phoneNumber;

	@Column(name = "address", length = 255)
	private String address;

	@Column(name = "social_media", columnDefinition = "jsonb")
	@Convert(converter = JsonConverter.class)
	private Map<SocialMediaPlatform, String> socialMedia;

	@Column(name = "created_at", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "updated_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}

}
