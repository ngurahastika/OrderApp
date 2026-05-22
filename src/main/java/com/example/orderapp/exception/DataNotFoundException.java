package com.example.orderapp.exception;

import java.util.List;
import java.util.Map;

import com.example.orderapp.constants.ErrorConstants;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataNotFoundException extends BaseRuntimeException {

	public DataNotFoundException() {
		super("404");
		this.message = ErrorConstants.ERR_DATA_NOT_FOUND;
	}

	public DataNotFoundException(String message) {
		super("404");
		this.message = message;

	}

	public DataNotFoundException(String message, Map<String, List<String>> errors) {
		super("404");
		this.message = message;
		this.errors = errors;
	}
}
