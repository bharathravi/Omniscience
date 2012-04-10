package com.omniscience.shared;

import java.io.Serializable;

public class IllegalUserException extends Exception implements Serializable {
	
	private String message;
	
	public IllegalUserException() {	
	}
	
	public IllegalUserException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
