package com.github.com.jorgdz.app.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.com.jorgdz.app.entity.Usuario;
import com.github.com.jorgdz.app.service.IUsuarioService;
import com.github.com.jorgdz.app.util.AppHelper;

@RestController
@CrossOrigin(origins = AppHelper.CROSS_ORIGIN, methods = {RequestMethod.GET})
@RequestMapping(AppHelper.PREFIX)
public class AuthController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUsuarioService serviceUsuario;
	
	@GetMapping(value = "/auth", produces = AppHelper.JSON)
	public ResponseEntity<Usuario> index (Principal principal)
	{
		String correo = principal.getName();
		log.info("Correo de usuario autenticado: " + correo);
		Usuario usuario = serviceUsuario.findByCorreo(correo);
		return new ResponseEntity<Usuario>(usuario, HttpStatus.ACCEPTED);
	}
}
