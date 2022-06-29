package com.meratransport.trip.exception;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
	private String errorMessage;
	private LocalDateTime time;
}
