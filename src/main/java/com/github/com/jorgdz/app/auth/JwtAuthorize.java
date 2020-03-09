package com.github.com.jorgdz.app.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.com.jorgdz.app.auth.granted.authority.SimpleGrantedAuthorityMix;
import com.github.com.jorgdz.app.util.AppHelper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtAuthorize extends BasicAuthenticationFilter{

	public JwtAuthorize(AuthenticationManager authenticationManager) 
	{
		super(authenticationManager);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader("Authorization");
		if(header == null || !header.startsWith("Bearer "))
		{
			chain.doFilter(request, response);
			return;
		}
		
		boolean tokenValid;
		Claims claims = null;
		try 
		{
			claims = Jwts.parser()
				.setSigningKey(AppHelper.SECRET_KEY)
				.parseClaimsJws(header.replace("Bearer ", "")).getBody();
			
			tokenValid = true;
		} 
		catch (JwtException | IllegalArgumentException e) 
		{
			tokenValid = false;
			logger.error("Error en autorización y validación del token: " + e.getMessage());
		}
		
		UsernamePasswordAuthenticationToken auth = null;
		
		if(tokenValid)
		{
			String email = claims.getSubject();
			Object permisos = claims.get("authorities");
			
			Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMix.class).readValue(permisos.toString().getBytes(), SimpleGrantedAuthority[].class));
			auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
		}
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		chain.doFilter(request, response);
	}
}
