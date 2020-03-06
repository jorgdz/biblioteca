package com.github.com.jorgdz.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.com.jorgdz.app.entity.Libro;

public interface ILibroService {
	
	Page<Libro> findAll(Pageable pageable);
}
