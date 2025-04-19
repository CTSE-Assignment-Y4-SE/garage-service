package com.garage.garage_service.core.payload.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageDto {

	private Object items;

	private int currentPage;

	private Long totalItems;

	private int totalPages;

	public PageDto() {
		this.currentPage = 0;
		this.totalItems = 0L;
		this.totalPages = 0;
	}

}
