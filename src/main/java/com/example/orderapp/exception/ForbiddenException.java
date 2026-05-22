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
public class ForbiddenException extends BaseRuntimeException {

	public ForbiddenException() {
		super("403");
		this.message = ErrorConstants.ERR_FORBIDDEN_ACCESS;
	}

	public ForbiddenException(String message, Map<String, List<String>> errors) {
		super("403");
	}

	public ForbiddenException(Map<String, List<String>> errors) {
		super("403");
		this.errors = errors;
		this.message = message;
	}

}
