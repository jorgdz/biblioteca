package com.github.com.jorgdz.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.com.jorgdz.app.entity.Usuario;
import com.github.com.jorgdz.app.service.IRolService;
import com.github.com.jorgdz.app.service.IUsuarioService;
import com.github.com.jorgdz.app.util.AppHelper;
import com.github.com.jorgdz.app.util.CustomResponse;
import com.github.com.jorgdz.app.util.ErrorResponse;
import com.github.com.jorgdz.app.util.PostResponse;
import com.github.com.jorgdz.app.util.paginator.Paginator;

@RestController
@CrossOrigin(origins = AppHelper.CROSS_ORIGIN, methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping(AppHelper.PREFIX)
public class UsuarioController {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUsuarioService serviceUsuario;
	
	@Autowired
	private IRolService serviceRol;
	
	// GET USERS
	@GetMapping(value = "/usuarios", produces = AppHelper.JSON)
	public ResponseEntity<?> index (@RequestParam(name = "page", required = false, defaultValue = "0") int page, @RequestParam(name = "size", required = false, defaultValue = "10") int size,			
			@RequestParam(name = "correo", required = false) Optional<String> correo)
	{
		if(correo.isPresent() && correo != null)
		{
			Usuario usuario = serviceUsuario.findByCorreo(correo.orElse(null));
			if(usuario == null)
			{
				return new ResponseEntity<>(new CustomResponse("El usuario con el correo "+correo.get()+" no existe"), HttpStatus.OK);
			}
			
			return new ResponseEntity<>(usuario, HttpStatus.OK);
		}
		
		Pageable pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
						
		Page<Usuario> usuarios = serviceUsuario.findAll(pageRequest);
		Paginator<Usuario> data = new Paginator<Usuario>(usuarios);
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
	
	// GET USER BY ID
	@GetMapping(value = "/usuarios/{id}", produces = AppHelper.JSON)
	public ResponseEntity<?> show (@PathVariable(name = "id", required = true) String idUsuario)
	{
		if(!AppHelper.validateLong(idUsuario))
		{
			return new ResponseEntity<>(new CustomResponse("El id del usuario debe ser numérico"), HttpStatus.CONFLICT);
		}
		
		Long id = Long.parseLong(idUsuario);
		
		if(id <= 0)
		{
			return new ResponseEntity<>(new CustomResponse("El id del usuario debe ser mayor a cero"), HttpStatus.CONFLICT);
		}
		
		Usuario usuario = serviceUsuario.findById(id);
		
		if(usuario == null)
		{
			return new ResponseEntity<>(new CustomResponse("No se ha encontrado ningún usuario para el ID " + id), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(usuario, HttpStatus.FOUND);
	}
	
	@PostMapping(value = "/usuarios", produces = AppHelper.JSON)
	public ResponseEntity<?> store (@Valid @RequestBody Usuario usuario, BindingResult br)
	{
		List<String> errors = new ArrayList<String>(); 
		// GET ERRORS
		if(br.hasErrors())
		{
			errors = br.getFieldErrors().stream()
					.map(error -> "El campo " + error.getField() + " " + error.getDefaultMessage())
					.collect(Collectors.toList());
			
			return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
		
		// VALIDATE UNIQUE EMAIL
		Usuario emailUnique = serviceUsuario.findByCorreo(usuario.getCorreo());
		if(emailUnique != null)
		{
			errors.add("El correo '" + usuario.getCorreo() + "' ya está en uso.");
			return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
		
		if(!usuario.getRoles().isEmpty())
		{
			usuario.setRoles(usuario.getRoles().stream().distinct().collect(Collectors.toSet()));
		}
		
		Usuario createUser = null;
		try
		{
			createUser = serviceUsuario.save(usuario);
		}
		catch(DataAccessException ex) {
			log.error(ex.getMessage());
			return new ResponseEntity<>(new CustomResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new PostResponse("Se ha agregado un nuevo usuario !!", createUser), HttpStatus.CREATED);
	}
}
