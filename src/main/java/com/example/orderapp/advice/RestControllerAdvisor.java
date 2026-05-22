package com.example.orderapp.advice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.orderapp.constants.ErrorConstants;
import com.example.orderapp.constants.LoggerConstant;
import com.example.orderapp.dto.BaseRes;
import com.example.orderapp.exception.AuthorizationException;
import com.example.orderapp.exception.BadRequestException;
import com.example.orderapp.exception.DataNotFoundException;
import com.example.orderapp.exception.ForbiddenException;
import com.example.orderapp.exception.ProcessingException;
import com.example.orderapp.exception.UnprocessableEntityException;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RestControllerAdvisor extends ResponseEntityExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(RestControllerAdvisor.class);

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<Object> handleAuthorizationException(AuthorizationException ex) {
		logger.error("AuthorizationException", ex);

		BaseRes body = new BaseRes<>();
		body.setReqId(MDC.get(LoggerConstant.REQ_ID));
		body.setMessage(ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
		logger.error("BadRequestException", ex);
		BaseRes body = new BaseRes<>();
		body.setReqId(MDC.get(LoggerConstant.REQ_ID));
		body.setMessage(ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<Object> handleBadRequest(ForbiddenException ex) {
		logger.error("ForbiddenException", ex);
		BaseRes body = new BaseRes<>();
		body.setReqId(MDC.get(LoggerConstant.REQ_ID));
		body.setMessage(ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(UnprocessableEntityException.class)
	public ResponseEntity<Object> handleUnprocessableEntity(UnprocessableEntityException ex) {
		logger.error("UnprocessableEntity", ex);

		BaseRes body = new BaseRes<>();
		body.setReqId(MDC.get(LoggerConstant.REQ_ID));
		body.setMessage(ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		// 1. Siapkan struktur Map untuk menampung list of errors per field
		Map<String, List<String>> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {
			String fieldName = error.getField();
			String errorMessage = error.getDefaultMessage();

			errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
		});

		BaseRes body = new BaseRes<>();
		body.setReqId(MDC.get(LoggerConstant.REQ_ID));
		body.setMessage(ErrorConstants.ERR_INVALID_FIELD);

		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<Object> handleDataNotFound(DataNotFoundException ex) {
		logger.error("DataNotFoundException", ex);
		BaseRes body = new BaseRes<>();
		body.setReqId(MDC.get(LoggerConstant.REQ_ID));
		body.setMessage(ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ProcessingException.class)
	public ResponseEntity<Object> handleProcessingException(ProcessingException ex) {
		logger.error("ProcessingException", ex);
		BaseRes body = new BaseRes<>();
		body.setReqId(MDC.get(LoggerConstant.REQ_ID));
		body.setMessage(ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		String msg = "";

		try {
			msg = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
			if (msg != null && msg.contains("\n")) {
				msg = msg.substring(0, msg.indexOf("\n"));
			}
		} catch (Exception e) {
			// ignore
		}

		logger.error("HttpMessageNotReadableException : {}", msg);

		BaseRes body = new BaseRes<>();
		body.setReqId(MDC.get(LoggerConstant.REQ_ID));
		body.setMessage("BadRequest");

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {

		BaseRes body = new BaseRes<>();
		body.setReqId(MDC.get(LoggerConstant.REQ_ID));
		body.setMessage("Data not found");

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGlobalException(Exception ex, HttpServletResponse response) throws IOException {
		logger.error("Unhandled Exception", ex);
		BaseRes body = new BaseRes<>();
		body.setReqId(MDC.get(LoggerConstant.REQ_ID));
		body.setMessage("Unexpected error occurred, please contact the administrator");

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
