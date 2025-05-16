package com.fresco.userservice.exceptionHandler;

import lombok.Data;

@Data
public class ExceptionResponse {

	private int status;
	private String error;
	private String message;
	private String path;
	private String code;
	private String type;
	
	public ExceptionResponse(int status, String error, String message, String path, String code, String type) {
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
		this.code = code;
		this.type = type;
	}

	public ExceptionResponse() {
		
	}

//
//	public String getType() {
//		return type;
//	}
//
//
//	public void setType(String type) {
//		this.type = type;
//	}
//
//
//	public int getStatus() {
//		return status;
//	}
//
//	public void setStatus(int status) {
//		this.status = status;
//	}
//
//	public String getError() {
//		return error;
//	}
//
//	public void setError(String error) {
//		this.error = error;
//	}
//
//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}
//
//	public String getPath() {
//		return path;
//	}
//
//	public void setPath(String path) {
//		this.path = path;
//	}
//
//	public String getCode() {
//		return code;
//	}
//
//	public void setCode(String code) {
//		this.code = code;
//	}
//
//	public ExceptionResponse(){
//
//	}
	
	

}
