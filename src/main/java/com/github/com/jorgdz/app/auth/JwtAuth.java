package com.github.com.jorgdz.app.auth;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.com.jorgdz.app.entity.Usuario;
import com.github.com.jorgdz.app.util.AppHelper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuth extends UsernamePasswordAuthenticationFilter {
	
	private AuthenticationManager authManager;
	
	public JwtAuth(AuthenticationManager authManager) 
	{
		this.authManager = authManager;
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(AppHelper.PREFIX.concat("/login"), "POST"));
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String correo = request.getParameter("correo");
		String clave = request.getParameter("clave");
				
		if(correo != null && clave != null)
		{
			logger.info("Email entry: " + correo);
			logger.info("Pass entry: " + clave);
		}
		else
		{
			Usuario usuario = null;
			try 
			{
				usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
				correo = usuario.getCorreo();
				clave = usuario.getClave();
				
				logger.info("Email entry -> InputStream: " + correo);
				logger.info("Pass -> InputStream: " + clave);
			}
			catch(JsonParseException ex)
			{
				logger.error(ex.getMessage());
			}
			catch (JsonMappingException e) 
			{
				logger.error(e.getMessage());
			} 
			catch (IOException e) 
			{
				logger.error(e.getMessage());
			}
		}
		
		correo = correo.trim();
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(correo, clave);
		return authManager.authenticate(token);
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String tokenJWT = this.getJwtToken(authResult);
		
		response.addHeader("Authorization", "Bearer ".concat(tokenJWT));
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("token", tokenJWT);
		data.put("usuario", (User) authResult.getPrincipal());
		data.put("success", "Bienvenido !!");
		this.responseJSON(response, data, 200);
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		
		Map<String, Object> error = new HashMap<String, Object>();
		error.put("unsuccess", "Ha ocurrido un error al intentar acceder, verifique que las credenciales sean correctas !!");
		error.put("error", failed.getMessage());
		this.responseJSON(response, error, 401);
	}
	
	public String getJwtToken (Authentication auth) throws IOException, ServletException
	{
		Collection<? extends GrantedAuthority> permisos = auth.getAuthorities();
		Claims claims = Jwts.claims();
		claims.put("authorities", new ObjectMapper().writeValueAsString(permisos));
		
		String tokenJWT = Jwts.builder()
				.setClaims(claims)
				.setSubject(auth.getName())
				.signWith(SignatureAlgorithm.HS512, AppHelper.SECRET_KEY)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + (3600000 * 2)))
				.compact();
		
		return tokenJWT;
	}
	
	public void responseJSON(HttpServletResponse resp, Map<String, Object> data, int status) throws IOException, ServletException
	{
		resp.getWriter().write(new ObjectMapper().writeValueAsString(data));
		resp.setContentType(AppHelper.JSON);
		resp.setStatus(status);
	}
}
