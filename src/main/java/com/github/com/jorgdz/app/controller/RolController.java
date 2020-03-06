package com.github.com.jorgdz.app.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.com.jorgdz.app.entity.Rol;
import com.github.com.jorgdz.app.service.IRolService;
import com.github.com.jorgdz.app.util.AppHelper;
import com.github.com.jorgdz.app.util.CustomResponse;
import com.github.com.jorgdz.app.util.paginator.Paginator;

@RestController
@CrossOrigin(AppHelper.CROSS_ORIGIN)
@RequestMapping(AppHelper.PREFIX)
public class RolController {
	
	@Autowired
	private IRolService serviceRol;
	
	@GetMapping(value = "/roles", produces = AppHelper.FORMAT_RESPONSE)
	public ResponseEntity<?> index (@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "name", required = false) Optional<String> nombre)
	{
		if(nombre.isPresent() && nombre != null)
		{
			Rol rol = serviceRol.findByNombre(nombre);
			if(rol == null)
			{
				return new ResponseEntity<>(new CustomResponse("El rol no existe"), HttpStatus.NOT_FOUND);
			}
			
			return new ResponseEntity<>(rol, HttpStatus.OK);
		}
		
		Page<Rol> roles = serviceRol.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
		Paginator<Rol> data = new Paginator<Rol>(roles);
		
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
	
}
