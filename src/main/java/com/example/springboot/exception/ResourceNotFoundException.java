package com.example.springboot.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(ResourceNotFoundException.class);
	public ResourceNotFoundException(String message) {
		super(message);
		logger.error("[Error - ResourceNotFoundException]" + message);
	}
}
