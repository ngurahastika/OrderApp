package com.example.orderapp.model;

import java.util.List;

import lombok.Getter;


@Getter
public class PageResult<T> {
	private List<T> results;
	private long totalRecords;
	private int totalPages;

	public PageResult(List<T> results, long totalRecords, long maxRow) {
		this.results = results;
		this.totalRecords = totalRecords;
		this.totalPages = maxRow <= 0 ? 0 : (int) Math.ceil((double) totalRecords / maxRow);
	}

}
