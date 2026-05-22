package com.example.orderapp.exception;

import java.util.List;
import java.util.Map;

import com.example.orderapp.constants.ErrorConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Setter
@Getter
public class UnprocessableEntityException extends BaseRuntimeException {

	public UnprocessableEntityException() {
		super("422");
		this.message = ErrorConstants.ERR_INVALID_FIELD;
	}

	public UnprocessableEntityException(String code) {
		super(code);
		this.code = code;
	}

	public UnprocessableEntityException(String message, Map<String, List<String>> errors) {
		super("422");

		this.message = message;
		this.errors = errors;
	}
	
	public UnprocessableEntityException(Map<String, List<String>> errors) {
		super("422");

		this.message = ErrorConstants.ERR_INVALID_FIELD;
		this.errors = errors;
	}

}
