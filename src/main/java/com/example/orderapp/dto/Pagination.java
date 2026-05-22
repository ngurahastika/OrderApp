package com.example.orderapp.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Pagination {

	private int page = 0;
	private int maxRow = 10;
	private int totalPage = 0;
	private long totalRecord;
	private String sortBy;
	private String sortDirection;
}
