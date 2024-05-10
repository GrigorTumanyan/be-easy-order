package com.ggit.orderstorage.exception;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException{

	@Serial
	private static final long serialVersionUID = 182158236987L;

	public UserNotFoundException(String exception) {
		super(exception);
	}
}
