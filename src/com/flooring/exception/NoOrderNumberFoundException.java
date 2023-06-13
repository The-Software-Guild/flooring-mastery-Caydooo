package com.flooring.exception;

@SuppressWarnings("serial")
public class NoOrderNumberFoundException extends RuntimeException {

	public NoOrderNumberFoundException(String msg) {
		super(msg);
	}
}
