package com.ggit.orderstorage.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 1L;

	public ConflictException(String exception) {
		super(exception);
	}

}
