package com.example.orderapp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseRes<T> {

	private String reqId;
	private String statusCode;
	private String statusDesc;
	private String message;
	private T data;
	private Pagination pagination;

}
