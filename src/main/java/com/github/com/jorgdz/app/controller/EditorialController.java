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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.com.jorgdz.app.entity.Editorial;
import com.github.com.jorgdz.app.service.IEditorialService;
import com.github.com.jorgdz.app.util.AppHelper;
import com.github.com.jorgdz.app.util.paginator.Paginator;

@RestController
@RequestMapping(AppHelper.PREFIX)
@CrossOrigin(origins = AppHelper.CROSS_ORIGIN, methods = {RequestMethod.GET})
public class EditorialController {
	
	@Autowired
	private IEditorialService serviceEditorial;
	
	@GetMapping(value = "/editoriales", produces = AppHelper.JSON)
	public ResponseEntity<?> index (@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size)
	{	
		Page<com.github.com.jorgdz.app.model.Editorial> editoriales = serviceEditorial.findAllEditorial(PageRequest.of(page, size, Sort.by("id").descending()));
		Paginator<com.github.com.jorgdz.app.model.Editorial> data = new Paginator<com.github.com.jorgdz.app.model.Editorial>(editoriales);
		
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/editorial/libros", produces = AppHelper.JSON)
	public ResponseEntity<?> getEditoriales (@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size)
	{	
		Page<Editorial> editoriales = serviceEditorial.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
		Paginator<Editorial> data = new Paginator<Editorial>(editoriales);
		
		return new ResponseEntity<>(data.paginate(), HttpStatus.OK);
	}
}
