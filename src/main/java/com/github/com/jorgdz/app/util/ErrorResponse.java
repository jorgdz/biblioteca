package com.github.com.jorgdz.app.util;

import java.io.Serializable;
import java.util.List;

public class ErrorResponse implements Serializable{

	private static final long serialVersionUID = 1966252883134320183L;
	
	private List<String> errors;

	public ErrorResponse(List<String> errors) {
		super();
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
}
