package com.github.com.jorgdz.app;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.com.jorgdz.app.auth.JwtAuth;
import com.github.com.jorgdz.app.auth.JwtAuthorize;
import com.github.com.jorgdz.app.service.UserDetailService;
import com.github.com.jorgdz.app.util.AppHelper;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
   	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailService serviceUserDetail;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.antMatchers("/", "/index").permitAll()
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/roles")).hasAuthority("ROLES_WITH_USERS_PERMISSIONS")
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/roles/**")).hasAuthority("ROLE_BY_ID_WITH_USERS_PERMISSIONS")
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/rol")).hasAuthority("ROL_SIMPLE")
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/rol/**")).hasAuthority("ROL_SIMPLE_BY_ID")
			.antMatchers(HttpMethod.POST, AppHelper.PREFIX.concat("/roles")).hasAuthority("CREATE_ROLES")
			.antMatchers(HttpMethod.PUT, AppHelper.PREFIX.concat("/roles/**")).hasAuthority("UPDATE_ROLES")
			.antMatchers(HttpMethod.PATCH, AppHelper.PREFIX.concat("/roles/**")).hasAuthority("UPDATE_ROLES")
			.antMatchers(HttpMethod.DELETE, AppHelper.PREFIX.concat("/roles/**")).hasAuthority("DELETE_ROLES")
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/permisos")).hasAuthority("PERMISSIONS")
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/permisos/**")).hasAuthority("PERMISSION_BY_ID")
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/editorial/libros")).hasAuthority("EDITORIALES_WITH_LIBROS")
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/editoriales")).hasAuthority("EDITORIALES")
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/libros")).hasAuthority("LIBROS")
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/usuarios")).hasAuthority("USUARIOS")
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/usuarios/**")).hasAuthority("USUARIOS_BY_ID")
			.antMatchers(HttpMethod.POST, AppHelper.PREFIX.concat("/usuarios")).hasAuthority("CREATE_USUARIOS")
			.antMatchers(HttpMethod.PUT, AppHelper.PREFIX.concat("/usuarios/**")).hasAuthority("UPDATE_USUARIOS")
			.antMatchers(HttpMethod.GET, AppHelper.PREFIX.concat("/auth")).authenticated()
			.anyRequest().authenticated()
			.and()
			.addFilter(new JwtAuth(this.authenticationManager())) 
			.addFilter(new JwtAuthorize(this.authenticationManager()))
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.cors();
		
		http.exceptionHandling().authenticationEntryPoint((request, response, e) -> {
	        Map<String, Object> error = new HashMap<>();
	        error.put("Hora", LocalDateTime.now().getDayOfMonth() + "-" + LocalDateTime.now().getMonth() + "-" + LocalDateTime.now().getYear() + " " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute()+ ":" + LocalDateTime.now().getSecond());
            error.put("mensaje", "Acceso denegado!!");
	        response.getWriter().write(new ObjectMapper().writeValueAsString(error));
	        response.setContentType(AppHelper.JSON);
	        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		});
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(serviceUserDetail).passwordEncoder(passwordEncoder);
	}
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() 
	{
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
