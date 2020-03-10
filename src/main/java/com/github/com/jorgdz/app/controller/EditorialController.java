package com.github.com.jorgdz.app.controller;

import java.util.ArrayList;
import java.util.List;
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

import com.github.com.jorgdz.app.entity.Editorial;
import com.github.com.jorgdz.app.service.IEditorialService;
import com.github.com.jorgdz.app.util.AppHelper;
import com.github.com.jorgdz.app.util.CustomResponse;
import com.github.com.jorgdz.app.util.ErrorResponse;
import com.github.com.jorgdz.app.util.PostResponse;
import com.github.com.jorgdz.app.util.paginator.Paginator;

@RestController
@RequestMapping(AppHelper.PREFIX)
@CrossOrigin(origins = AppHelper.CROSS_ORIGIN, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})
public class EditorialController {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IEditorialService serviceEditorial;
	
	// GET EDITORIALES
	@GetMapping(value = "/editoriales", produces = AppHelper.JSON)
	public ResponseEntity<?> index (@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size)
	{	
		Page<com.github.com.jorgdz.app.model.Editorial> editoriales = serviceEditorial.findAllEditorial(PageRequest.of(page, size, Sort.by("id").descending()));
		Paginator<com.github.com.jorgdz.app.model.Editorial> data = new Paginator<com.github.com.jorgdz.app.model.Editorial>(editoriales);
		
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
	
	// GET EDITORIALES WITH LIBROS
	@GetMapping(value = "/editorial/libros", produces = AppHelper.JSON)
	public ResponseEntity<?> getEditoriales (@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size)
	{	
		Page<Editorial> editoriales = serviceEditorial.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
		Paginator<Editorial> data = new Paginator<Editorial>(editoriales);
		
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
		
	// GET EDITORIAL BY ID
	@GetMapping(value = "/editoriales/{id}", produces = AppHelper.JSON)
	public ResponseEntity<?> show (@PathVariable(name = "id", required = true) String idEditorial)
	{
		if(!AppHelper.validateLong(idEditorial))
		{
			return new ResponseEntity<>(new CustomResponse("El id del editorial debe ser numérico."), HttpStatus.CONFLICT);
		}
		
		Long id = Long.parseLong(idEditorial);
		
		if(id <= 0)
		{
			return new ResponseEntity<>(new CustomResponse("El id debe ser numérico."), HttpStatus.CONFLICT);
		}
		
		Editorial editorial = serviceEditorial.findById(id);
		
		if(editorial == null)
		{
			return new ResponseEntity<>(new CustomResponse("No existe el editorial con ID: ".concat(idEditorial)), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(editorial, HttpStatus.FOUND);
	}
	
	
	//SAVE EDITORIAL
	@PostMapping(value = "/editoriales", produces = AppHelper.JSON)
	public ResponseEntity<?> store (@Valid @RequestBody Editorial editorial, BindingResult br)
	{
		List<String> errors = new ArrayList<String>(); 
		if(br.hasErrors())
		{
			errors = br.getFieldErrors().stream().map(error -> "El campo " + error.getField() + " " + error.getDefaultMessage()).collect(Collectors.toList());
			return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
		
		// VALIDATE UNIQUE EDITORIAL
		Editorial uniqueEditorial = serviceEditorial.findByNombre(editorial.getNombre());
		if(uniqueEditorial != null)
		{
			errors.add("El editorial '" + editorial.getNombre() + "' ya existe en nuestros registros.");
			return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.CONFLICT);
		}
		
		Editorial newEditorial = null;
		try {
			newEditorial = serviceEditorial.save(editorial);
		} 
		catch (DataAccessException e)
		{
			log.error(e.getMessage());
			return new ResponseEntity<>(new CustomResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e) 
		{
			log.error(e.getMessage());
			return new ResponseEntity<>(new CustomResponse("Excepción en el servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new PostResponse("Se ha creado una nueva editorial.", newEditorial), HttpStatus.CREATED);
	}
	
	
	// UPDATE EDITORIAL
	@RequestMapping(value = "/editoriales/{id}", method = {RequestMethod.PATCH, RequestMethod.PUT}, produces = AppHelper.JSON)
	public ResponseEntity<?> update (@Valid @RequestBody Editorial editorial, BindingResult br, @PathVariable(name = "id", required = true) String idEditorial)
	{
		List<String> errors = new ArrayList<String>(); 
		
		// VALIDATE ID NUMERIC
		if(!AppHelper.validateLong(idEditorial))
		{
			return new ResponseEntity<CustomResponse>(new CustomResponse("El id del editorial debe ser numérico."), HttpStatus.CONFLICT);
		}
		
		// GET ERRORS
		if(br.hasErrors())
		{
			errors = br.getFieldErrors().stream().map(error -> "El campo " + error.getField() + " " + error.getDefaultMessage()).collect(Collectors.toList());	
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
		
		// VERIFY EXIST EDITORIAL IN DATA
		Long id = Long.parseLong(idEditorial);
		Editorial currentEditorial = serviceEditorial.findById(id);
		if(currentEditorial == null)
		{
			return new ResponseEntity<CustomResponse>(new CustomResponse("No se ha encontrado ninguna editorial para el ID " + id), HttpStatus.NOT_FOUND);
		}
		
		// VALIDATE UNIQUE EDITORIAL
		Editorial uniqueEditorial = serviceEditorial.findByNombre(editorial.getNombre(), id);
		if(uniqueEditorial != null)
		{
			errors.add("La editorial ya existe en nuestros registros !!");
			return new ResponseEntity<ErrorResponse>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
		}
		
		Editorial updateEditorial = null;
		try 
		{
			currentEditorial.setId(id);
			currentEditorial.setNombre(editorial.getNombre());
			
			serviceEditorial.save(currentEditorial);
			updateEditorial = serviceEditorial.findById(id);
		}
		catch (DataAccessException e) 
		{
			log.info("Error al actualizar editorial: ".concat(e.getMessage()));
			return new ResponseEntity<CustomResponse>(new CustomResponse("Error interno del servidor."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception ex)
		{
			log.error(ex.getLocalizedMessage());
			return new ResponseEntity<CustomResponse>(new CustomResponse("Excepción del servidor."), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<PostResponse>(new PostResponse("Editorial modificada !!", updateEditorial), HttpStatus.OK);
	}
	
	// DELETE EDITORIAL
	@DeleteMapping(value = "/editoriales/{id}", produces = AppHelper.JSON)
	public ResponseEntity<?> destroy (@PathVariable(name = "id", required = true) String idEditorial)
	{
		if(!AppHelper.validateLong(idEditorial))
		{
			return new ResponseEntity<CustomResponse>(new CustomResponse("El id del editorial debe ser numérico"), HttpStatus.CONFLICT);
		}
		
		Long id = Long.parseLong(idEditorial);
		
		Editorial editorial = serviceEditorial.findById(id);
		if(editorial == null)
		{
			return new ResponseEntity<CustomResponse>(new CustomResponse("No hemos encontrado ningún editorial con el ID: " + id), HttpStatus.NOT_FOUND);
		}
		
		// DELETE
		serviceEditorial.deleteById(id);
		return new ResponseEntity<CustomResponse>(new CustomResponse("Se ha borrado el editorial " + editorial.getNombre() +"."), HttpStatus.OK);
	}
}
