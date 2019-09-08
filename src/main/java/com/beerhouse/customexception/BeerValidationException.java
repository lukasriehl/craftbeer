package com.beerhouse.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerValidationException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public BeerValidationException(String errorMessage) {
        super(errorMessage);
    }
}
