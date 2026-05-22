package com.example.orderapp.exception;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseRuntimeException extends RuntimeException {
	

	protected String code;
	protected Map<String,List<String>> errors;
	protected String message;

	public BaseRuntimeException(String code) {
		super(code);
	}

	public String toString() {
		return this.getClass().getSimpleName() + " : " + this.code;
	}
}
