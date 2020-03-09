package com.github.com.jorgdz.app.auth.granted.authority;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityMix {
	
	@JsonCreator
	public SimpleGrantedAuthorityMix(@JsonProperty("authority") String permiso) {}
}
