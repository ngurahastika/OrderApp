package com.example.orderapp.exception;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public class BadRequestException extends BaseRuntimeException {

	public BadRequestException() {
		super("400");
	}

	public BadRequestException(String message) {
		super("400");
		this.message = message;
	}

	public BadRequestException(String message, Map<String, List<String>> errors) {
		super("400");
	}

	public BadRequestException(Map<String, List<String>> errors) {
		super("400");
		this.errors = errors;
		this.message = message;
	}

}
