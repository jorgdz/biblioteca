package com.github.com.jorgdz.app.controller;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.com.jorgdz.app.entity.Rol;
import com.github.com.jorgdz.app.service.IPermisoService;
import com.github.com.jorgdz.app.service.IRolService;
import com.github.com.jorgdz.app.util.AppHelper;
import com.github.com.jorgdz.app.util.CustomResponse;
import com.github.com.jorgdz.app.util.ErrorResponse;
import com.github.com.jorgdz.app.util.PostResponse;
import com.github.com.jorgdz.app.util.paginator.Paginator;

@RestController
@CrossOrigin(origins = AppHelper.CROSS_ORIGIN, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(AppHelper.PREFIX)
public class RolController {
	
	@Autowired
	private IRolService serviceRol;
	
	@Autowired
	private IPermisoService servicePermiso;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	// GET ROLES WITH USERS AND PERMISSIONS
	@GetMapping(value = "/roles", produces = AppHelper.JSON)
	public ResponseEntity<?> index (@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "name", required = false) Optional<String> nombre)
	{
		Page<Rol> roles = serviceRol.findAllByNombre(nombre, PageRequest.of(page, size, Sort.by("id").descending()));
		Paginator<Rol> data = new Paginator<Rol>(roles);
		
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
		
	// GET BY ID ROLES WITH USERS AND PERMISSIONS
	@GetMapping(value = "/roles/{id}", produces = AppHelper.JSON)
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
	@GetMapping(value = "/rol", produces = AppHelper.JSON)
	public ResponseEntity<?> getRoles (@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "name", required = false) Optional<String> nombre)
	{
		Page<com.github.com.jorgdz.app.model.Rol> roles = serviceRol.findAll(nombre, PageRequest.of(page, size, Sort.by("id").descending()));
		Paginator<com.github.com.jorgdz.app.model.Rol> data = new Paginator<com.github.com.jorgdz.app.model.Rol>(roles);
		
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
	
	// GET ROLES BY ID
	@GetMapping(value = "/rol/{id}", produces = AppHelper.JSON)
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
	
	
	@PostMapping(value = "/roles", produces = AppHelper.JSON)
	public ResponseEntity<?> store (@Valid @RequestBody Rol rol, BindingResult br)
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
		
		// VALIDATE UNIQUE ROL
		Rol rolUnique = serviceRol.findByNombre(rol.getNombre());
		
		if(rolUnique != null)
		{
			errors.add("El rol '" + rol.getNombre() + "' ya existe.");
			return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
		
		if(!rol.getPermisos().isEmpty())
		{
			rol.setPermisos(rol.getPermisos().stream().distinct().collect(Collectors.toList()));
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
			return new ResponseEntity<>(new CustomResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new PostResponse("Se ha agregado un nuevo rol !!", role), HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/roles/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH}, produces = AppHelper.JSON)
	public ResponseEntity<?> update (@Valid @RequestBody Rol rol, BindingResult br, @PathVariable(name = "id", required = true) String idRol)
	{
		List<String> errors = new ArrayList<String>(); 
		// Validate idRol numeric
		if(!AppHelper.validateLong(idRol))
		{
			return new ResponseEntity<>(new CustomResponse("El id del rol debe ser numérico"), HttpStatus.CONFLICT);
		}
		
		// GET ERRORS IN COLUMNS
		if(br.hasErrors())
		{
			errors = br.getFieldErrors().stream()
					.map(error -> "El campo " + error.getField() + " " + error.getDefaultMessage())
					.collect(Collectors.toList());
			
			return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
		
		Long id = Long.parseLong(idRol);
		
		// FIND ROL BY ID FOR UPDATE
		Rol role = serviceRol.findById(id);
		
		if(role == null)
		{
			return new ResponseEntity<>(new CustomResponse("No se ha encontrado ningún rol para el ID " + id), HttpStatus.NOT_FOUND);
		}
		
		// VALIDATE UNIQUE ROL
		Rol rolUnique = serviceRol.findByNombre(rol.getNombre(), id);
		
		if(rolUnique != null)
		{
			errors.add("El rol '" + rol.getNombre() + "' ya existe.");
			return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
		
		if(!rol.getPermisos().isEmpty())
		{
			rol.setPermisos(rol.getPermisos().stream().distinct().collect(Collectors.toList()));
		}
		
		Rol updateRol = null;
		
		try
		{
			Long [] permisos_id_req = rol.getPermisos().stream().map(p -> p.getId()).sorted().toArray(Long[]::new);
			Long [] permisos_id_data = role.getPermisos().stream().map(p -> p.getId()).sorted().toArray(Long[]::new);
			
			log.info("Request: " + Arrays.toString(permisos_id_req));
			log.info("Data: " + Arrays.toString(permisos_id_data));
			
			// UPDATE ROL
			role.setId(id);
			role.setNombre(rol.getNombre());
				
			// AÑADIENDO PERMISOS AL ROL
			role.setPermisos(rol.getPermisos().stream().map(p -> servicePermiso.findById(p.getId())).collect(Collectors.toList()));
										
			serviceRol.save(role);
			updateRol = serviceRol.findById(id);
		}
		catch(DataAccessException ex)
		{
			log.error(ex.getMessage());
			return new ResponseEntity<>(new CustomResponse("Error interno del servidor."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception ex)
		{
			log.error(ex.getMessage());
			return new ResponseEntity<>(new CustomResponse("Excepción del servidor."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new PostResponse("Datos de rol modificado !!", updateRol), HttpStatus.OK);
	}
	
	
	@DeleteMapping(value = "/roles/{id}", produces = AppHelper.JSON)
	public ResponseEntity<?> destroy (@PathVariable(name = "id", required = true) String idRol)
	{
		// Validate idRol numeric
		if(!AppHelper.validateLong(idRol))
		{
			return new ResponseEntity<>(new CustomResponse("El id del rol debe ser numérico"), HttpStatus.CONFLICT);
		}
		
		Long id = Long.parseLong(idRol);
		
		Rol rol = serviceRol.findById(id);
		if(rol == null)
		{
			return new ResponseEntity<>(new CustomResponse("No se ha encontrado ningún rol para el ID " + id), HttpStatus.NOT_FOUND);
		}
		
		serviceRol.delete(id);
		
		return new ResponseEntity<>(new CustomResponse("Se eliminado el rol " + rol.getNombre() + "!!"), HttpStatus.OK);
	}
}
