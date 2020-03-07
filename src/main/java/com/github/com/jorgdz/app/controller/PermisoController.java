package com.github.com.jorgdz.app.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.com.jorgdz.app.entity.Permiso;
import com.github.com.jorgdz.app.service.IPermisoService;
import com.github.com.jorgdz.app.util.AppHelper;
import com.github.com.jorgdz.app.util.CustomResponse;
import com.github.com.jorgdz.app.util.paginator.Paginator;

@RestController
@CrossOrigin(AppHelper.CROSS_ORIGIN)
@RequestMapping(AppHelper.PREFIX)
public class PermisoController {
	
	@Autowired
	private IPermisoService servicePermiso;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	 // GET PERMISSIONS WITH ALL
	@GetMapping(value = "/permisos", produces = AppHelper.FORMAT_RESPONSE)
	public ResponseEntity<?> index (@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "name", required = false) Optional<String> nombre)
	{
		if(nombre.isPresent())
		{
			log.info("Búsqueda: " + nombre.get());
		}
		
		Page<Permiso> permisos = servicePermiso.findAll(nombre, PageRequest.of(page, size, Sort.by("id").descending()));
		Paginator<Permiso> data = new Paginator<Permiso>(permisos);
		
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
	
	// GET PERMISSION BY ID WITH ALL
	@GetMapping(value = "/permisos/{id}", produces = AppHelper.FORMAT_RESPONSE)
	public ResponseEntity<?> show (@PathVariable(name = "id") String idPermiso)
	{
		if(!AppHelper.validateLong(idPermiso))
		{
			return new ResponseEntity<>(new CustomResponse("El id del permiso debe ser numérico"), HttpStatus.CONFLICT);
		}
		
		Long id = Long.parseLong(idPermiso);
		
		if(id <= 0)
		{
			return new ResponseEntity<>(new CustomResponse("El id del permiso debe ser mayor a cero"), HttpStatus.CONFLICT);
		}
		
		Permiso permiso = servicePermiso.findById(id);
		
		if(permiso == null)
		{
			return new ResponseEntity<>(new CustomResponse("No se ha encontrado ningún permiso para el ID " + id), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(permiso, HttpStatus.FOUND);
	}
}
