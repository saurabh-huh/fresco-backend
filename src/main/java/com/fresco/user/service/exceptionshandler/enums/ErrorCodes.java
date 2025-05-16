package com.fresco.user.service.exceptionshandler.enums;

public enum ErrorCodes {
	INVALID_DATA("DTC0001"),
	NOT_NULL_VALIDATION("DTC0002"),
	CDC_EXCEPTION("DTC0003"),
	CONNECTION_FAILED("DTC0005");








	private String value;

	public String getValue() {
		return value;
	}


	private ErrorCodes(String value) {
		this.value = value;
	}

	
	
}
