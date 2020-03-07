package com.github.com.jorgdz.app.util;

public class AppHelper {
	
	public static final String CROSS_ORIGIN = "*";
	public static final String FORMAT_RESPONSE = "application/json";
	public static final String PREFIX = "/api";
	
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
