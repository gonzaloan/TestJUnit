package com.sodexo.junit.springjunittest.exception;

public class ArrivalException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public ArrivalException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
//	
//	public ArrivalException() {
//		super();
//	}

}
