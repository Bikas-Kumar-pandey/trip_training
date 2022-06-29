package com.meratransport.trip.exception;

import java.time.LocalDateTime;

import com.meratransport.trip.report.exception.ReportException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) throws Exception {
		var errorResponse = ErrorResponse.builder().errorMessage(ex.getMessage()).time(LocalDateTime.now()).build();
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(ReportException.class)
	public ResponseEntity<ErrorResponse> handleReportException(ReportException ex){
		var errorResponse = ErrorResponse.builder().errorMessage(ex.getMessage()).time(LocalDateTime.now()).build();
		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
