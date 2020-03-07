package com.github.com.jorgdz.app.util;

public class PostResponse {
	
	private String message;
	
	private Object created;
	
	public PostResponse (String msg, Object object)
	{
		this.created = object;
		this.message = msg;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return created;
	}

	public void setData(Object data) {
		this.created = data;
	}
	
}
