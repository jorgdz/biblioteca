package com.github.com.jorgdz.app.util;

import java.io.Serializable;

public class CustomResponse implements Serializable {
	
	private static final long serialVersionUID = 6192447355861761112L;
	
	private String message;

	public CustomResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
		
}
