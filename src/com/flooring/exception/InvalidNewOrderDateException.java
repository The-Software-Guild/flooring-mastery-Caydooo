package com.flooring.exception;

@SuppressWarnings("serial")
public class InvalidNewOrderDateException extends RuntimeException {

	public InvalidNewOrderDateException(String msg) {
		super(msg);
	}
}
