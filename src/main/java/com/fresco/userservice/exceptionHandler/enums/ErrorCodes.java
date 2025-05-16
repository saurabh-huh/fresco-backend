package com.fresco.userservice.exceptionHandler.enums;

public enum ErrorCodes {
	INVALID_DATA("FK0001"),
	NOT_NULL_VALIDATION("FK0002"),
	FK_EXCEPTION("FK0003"),
	CONNECTION_FAILED("FK0004"),
	ENCRYPTION_FAILED("FK0005"),
	INCORRECT_CREDENTIALS("FK0006"),
	USER_NOT_FOUND("FK0007");








	private String value;

	public String getValue() {
		return value;
	}


	private ErrorCodes(String value) {
		this.value = value;
	}

	
	
}
