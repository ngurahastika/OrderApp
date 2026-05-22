package com.example.orderapp.exception;

import java.util.List;
import java.util.Map;

public class ProcessingException extends BaseRuntimeException {

	public ProcessingException() {
		super("500");
	}

	public ProcessingException(String message, Map<String, List<String>> errors) {
		super("500");

		this.code = code;
		this.message = message;
		this.errors = errors;

	}

}
