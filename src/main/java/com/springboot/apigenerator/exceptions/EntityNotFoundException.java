package com.springboot.apigenerator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No record found for this id.")
public class EntityNotFoundException extends Exception {
	
private static final long serialVersionUID = 1L;
	
	public EntityNotFoundException(String message) {
		super(message);
	};


}
