package com.github.com.jorgdz.app.util;

public class AppHelper {
	
	public static final String CROSS_ORIGIN = "*";
	public static final String JSON = "application/json;charset=UTF-8";
	public static final String PREFIX = "/api";
	
	public static final byte[] SECRET_KEY = "my.secret.key".getBytes();
	
	public static boolean validateLong(String n)
	{
		boolean validate = false;
		try 
		{
			Long.parseLong(n);
			validate = true;
		} catch (NumberFormatException e) 
		{
			validate = false;
		}
		
		return validate;
	}
}
