package com.github.com.jorgdz.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.com.jorgdz.app.entity.Libro;
import com.github.com.jorgdz.app.repository.LibroRepo;

@Service
public class LibroService implements ILibroService{

	@Autowired
	private LibroRepo repoLibro;
	
	@Override
	public Page<Libro> findAll(Pageable pageable) {
		return repoLibro.findAll(pageable);
	}
	
	
}
