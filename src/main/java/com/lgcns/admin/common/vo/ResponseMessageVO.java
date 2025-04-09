package com.lgcns.admin.common.vo;

import lombok.Data;

@Data
public class ResponseMessageVO {
	private int error;
	private String message;
	private Object result;
	
	public ResponseMessageVO() {
		this.error = 0;
		this.message = null;
		this.result = null;
	}
	
	public ResponseMessageVO(Object result) {
		this.error = 0;
		this.message = null;
		this.result = result;
	}
	
	public ResponseMessageVO(int error, String message) {
		this.error = error;
		this.message = message;
	}
	
	public ResponseMessageVO(int error, String message, Object result) { 
		this.error = error;
		this.message = message;
		this.result = result;
	}

	@Override
	public String toString() {
		return "ResponseMessageVO{" +
				"error=" + error +
				", message='" + message + '\'' +
				'}';
	}
}
