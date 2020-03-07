package com.github.com.jorgdz.app.controller;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.com.jorgdz.app.entity.Rol;
import com.github.com.jorgdz.app.service.IRolService;
import com.github.com.jorgdz.app.util.AppHelper;
import com.github.com.jorgdz.app.util.CustomResponse;
import com.github.com.jorgdz.app.util.ErrorResponse;
import com.github.com.jorgdz.app.util.PostResponse;
import com.github.com.jorgdz.app.util.paginator.Paginator;

@RestController
@CrossOrigin(AppHelper.CROSS_ORIGIN)
@RequestMapping(AppHelper.PREFIX)
public class RolController {
	
	@Autowired
	private IRolService serviceRol;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	// GET ROLES WITH USERS AND PERMISSIONS
	@GetMapping(value = "/roles", produces = AppHelper.FORMAT_RESPONSE)
	public ResponseEntity<?> index (@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "name", required = false) Optional<String> nombre)
	{
		Page<Rol> roles = serviceRol.findAllByNombre(nombre, PageRequest.of(page, size, Sort.by("id").descending()));
		Paginator<Rol> data = new Paginator<Rol>(roles);
		
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
		
	// GET BY ID ROLES WITH USERS AND PERMISSIONS
	@GetMapping(value = "/roles/{id}", produces = AppHelper.FORMAT_RESPONSE)
	public ResponseEntity<?> show (@PathVariable(name = "id") String idRol)
	{
		if(!AppHelper.validateLong(idRol))
		{
			return new ResponseEntity<>(new CustomResponse("El id del rol debe ser numérico"), HttpStatus.CONFLICT);
		}
		
		Long id = Long.parseLong(idRol);
		
		if(id <= 0)
		{
			return new ResponseEntity<>(new CustomResponse("El id del rol debe ser mayor a cero"), HttpStatus.CONFLICT);
		}
		
		Rol rol = serviceRol.findById(id);
		
		if(rol == null)
		{
			return new ResponseEntity<>(new CustomResponse("No se ha encontrado ningún rol para el ID " + id), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(rol, HttpStatus.FOUND);
	}
	
	// GET ROLES
	@GetMapping(value = "/rol", produces = AppHelper.FORMAT_RESPONSE)
	public ResponseEntity<?> getRoles (@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "name", required = false) Optional<String> nombre)
	{
		Page<Rol> roles = serviceRol.findAll(nombre, PageRequest.of(page, size, Sort.by("id").descending()));
		Paginator<Rol> data = new Paginator<Rol>(roles);
		
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
	
	// GET ROLES BY ID
	@GetMapping(value = "/rol/{id}", produces = AppHelper.FORMAT_RESPONSE)
	public ResponseEntity<?> getRolById (@PathVariable(name = "id") String idRol)
	{
		if(!AppHelper.validateLong(idRol))
		{
			return new ResponseEntity<>(new CustomResponse("El id del rol debe ser numérico"), HttpStatus.CONFLICT);
		}
		
		Long id = Long.parseLong(idRol);
		
		if(id <= 0)
		{
			return new ResponseEntity<>(new CustomResponse("El id del rol debe ser mayor a cero"), HttpStatus.CONFLICT);
		}
		
		Rol rol = null;
		try 
		{
			rol = serviceRol.findRolById(id);
		} 
		catch (DataAccessException e) 
		{
			log.error("Error de DB: " + e.getMessage());
			return new ResponseEntity<>(new CustomResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(rol == null)
		{
			return new ResponseEntity<>(new CustomResponse("No se ha encontrado ningún rol para el ID " + id), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(rol, HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/roles", produces = AppHelper.FORMAT_RESPONSE)
	public ResponseEntity<?> store (@Valid @RequestBody Rol rol, BindingResult br)
	{
		// GET ERRORS
		if(br.hasErrors())
		{
			List<String> errors = br.getFieldErrors().stream()
					.map(error -> "El campo " + error.getField() + " " + error.getDefaultMessage())
					.collect(Collectors.toList());
			
			return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
		
		Rol role = null;
		try
		{
			// CREATE ROL AND ASSIGN PERMISSION
			role = serviceRol.save(rol);
		}
		catch(DataAccessException ex)
		{
			log.error(ex.getMessage());
			String [] err = ex.getCause().getCause().getLocalizedMessage().split("Detail: ");
			List<String> error = Arrays.asList(err[1]);
			return new ResponseEntity<>(new ErrorResponse(error), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new PostResponse("Se ha agregado un nuevo rol !!", role), HttpStatus.CREATED);
	}
}
