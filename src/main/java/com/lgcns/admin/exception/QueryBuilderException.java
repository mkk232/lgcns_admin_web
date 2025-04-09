package com.lgcns.admin.exception;


public class QueryBuilderException extends Exception {
	private static final long serialVersionUID = 1L;
	private int errorCode;

	public int getErrorCode() {
		return this.errorCode;
	}

	public QueryBuilderException(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public QueryBuilderException(String message) {
		super(message);
	}
	
	public QueryBuilderException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

}
