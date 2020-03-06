package com.github.com.jorgdz.app.controller;

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

import com.github.com.jorgdz.app.entity.Libro;
import com.github.com.jorgdz.app.service.ILibroService;
import com.github.com.jorgdz.app.util.AppHelper;
import com.github.com.jorgdz.app.util.paginator.Paginator;

@RestController
@RequestMapping(AppHelper.PREFIX)
@CrossOrigin(AppHelper.CROSS_ORIGIN)
public class LibroController {
	
	@Autowired
	private ILibroService serviceLibro;
	
	@GetMapping(value = "/libros", produces = AppHelper.FORMAT_RESPONSE)
	public ResponseEntity<?> index (@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size)
	{
		Page<Libro> libros = serviceLibro.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
		Paginator<Libro> data = new Paginator<Libro>(libros);
		
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
}
