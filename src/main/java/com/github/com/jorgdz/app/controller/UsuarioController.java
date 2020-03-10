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

import com.github.com.jorgdz.app.entity.Rol;
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
			@RequestParam(name = "nombres", required = false) Optional<String> nombres,
			@RequestParam(name = "apellidos", required = false) Optional<String> apellidos,
			@RequestParam(name = "correo", required = false) Optional<String> correo,
			@RequestParam(name = "enabled", required = false) Optional<Boolean> enabled)
	{		
		Pageable pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
						
		Page<Usuario> usuarios = serviceUsuario.findAll(nombres, apellidos, correo, enabled, pageRequest);
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
	
	
	//SAVE USER
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
		
		usuario.setCorreo(usuario.getCorreo().toLowerCase());
		
		// VALIDATE UNIQUE EMAIL
		Usuario emailUnique = serviceUsuario.findByCorreo(usuario.getCorreo());
		if(emailUnique != null)
		{
			errors.add("El correo '" + usuario.getCorreo() + "' ya está en uso.");
			return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
				
		if(!usuario.getRoles().isEmpty())
		{
			usuario.setRoles(usuario.getRoles().stream().distinct().collect(Collectors.toList()));
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
	
	
	//UPDATE USER
	@RequestMapping(value = "/usuarios/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH}, produces = AppHelper.JSON)
	public ResponseEntity<?> update (@Valid @RequestBody Usuario usuario, BindingResult br, @PathVariable(name = "id", required = true) String idUsuario)
	{
		List<String> errors = new ArrayList<String>(); 
		
		// Validate idUsuario numeric
		if(!AppHelper.validateLong(idUsuario))
		{
			return new ResponseEntity<>(new CustomResponse("El id del usuario debe ser numérico"), HttpStatus.CONFLICT);
		}
				
		// GET ERRORS
		if(br.hasErrors())
		{
			errors = br.getFieldErrors().stream()
					.map(error -> "El campo " + error.getField() + " " + error.getDefaultMessage())
					.collect(Collectors.toList());
			
			return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
		
		Long id = Long.parseLong(idUsuario);
		
		// FIND USER BY ID FOR UPDATE
		Usuario user = serviceUsuario.findById(id);
		if(user == null)
		{
			return new ResponseEntity<>(new CustomResponse("No se ha encontrado ningún usuario para el ID " + id), HttpStatus.NOT_FOUND);
		}

		usuario.setCorreo(usuario.getCorreo().toLowerCase());
		
		// VALIDATE UNIQUE EMAIL
		Usuario emailUnique = serviceUsuario.findByCorreo(usuario.getCorreo(), id);
		if(emailUnique != null)
		{
			errors.add("El correo '" + usuario.getCorreo() + "' ya está en uso.");
			return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
		
		if(!usuario.getRoles().isEmpty())
		{
			usuario.getRoles().forEach(role -> {
				System.out.println(role);
 		 	});
			//usuario.setRoles(usuario.getRoles().stream().distinct().collect(Collectors.toList()));  // VERIFICANDO QUE LOS ROLES NO SE REPITAN EN EL REQUEST
		}
		
		Usuario updateUsuario = null;
		try 
		{
			Long [] roles_req_id = usuario.getRoles().stream().map(r -> r.getId()).sorted().distinct().toArray(Long[]::new);
			Long [] roles_data_id = user.getRoles().stream().map(r -> r.getId()).sorted().toArray(Long[]::new);
			
			log.info("Roles request: " + Arrays.toString(roles_req_id));
			log.info("Roles data: " + Arrays.toString(roles_data_id));
			
			// UPDATE
			user.setId(id);
			user.setNombres(usuario.getNombres());
			user.setApellidos(usuario.getApellidos());
			user.setCorreo(usuario.getCorreo());
			user.setEnabled(usuario.isEnabled());
			user.setClave(usuario.getClave());
			
			if(roles_req_id.length > 0 && roles_req_id != null) {
				for (Long rolId : roles_req_id) 
				{
					if(!Arrays.asList(roles_data_id).contains(rolId))
					{
						// ADD ROL FOR USER
						user.setRoles(usuario.getRoles().stream().map(u -> serviceRol.findById(u.getId())).collect(Collectors.toList()));
					}
				}
			}
			
			if(!user.getRoles().isEmpty())
			{
				if(roles_req_id.length == 0 || roles_req_id == null)
				{
					// DELETE ALL ROLES FOR USER
					for (Rol rol : user.getRoles()) {
						serviceRol.deleteRolUsuarioById(id, rol.getId());
					}
					user.clear();
				}
				else
				{
					// BUG ENCONTRADO SI MANDO 1 ROL EN REQUEST Y EN DATA TENGO 3 ROLES ASIGNADOS ME MANDA UN ERROR
					for (Rol rol : user.getRoles()) 
					{
						if(!Arrays.asList(roles_req_id).contains(rol.getId()))
						{
							serviceRol.deleteRolUsuarioById(id, rol.getId());
							user.removeRol(rol);
						}
					}
				}
			}
			
			serviceUsuario.save(user);
			updateUsuario = serviceUsuario.findById(id);
		}
		catch (DataAccessException e) 
		{
			log.info("Error al actualizar usuario: ".concat(e.getMessage()));
			return new ResponseEntity<>(new CustomResponse("Error interno del servidor."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception ex)
		{
			log.error(ex.getLocalizedMessage());
			return new ResponseEntity<>(new CustomResponse("Excepción del servidor."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new PostResponse("Datos de usuario modificados !!", updateUsuario), HttpStatus.OK);
	}
}
