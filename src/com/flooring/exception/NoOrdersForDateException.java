package com.flooring.exception;

@SuppressWarnings("serial")
public class NoOrdersForDateException extends RuntimeException {

	public NoOrdersForDateException(String msg) {
		super(msg);
	}
}
