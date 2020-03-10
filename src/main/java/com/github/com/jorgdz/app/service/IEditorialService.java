package com.github.com.jorgdz.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.com.jorgdz.app.entity.Editorial;

public interface IEditorialService {
	
	Page<Editorial> findAll(Pageable pageable);
	
	Editorial findById(Long id);
	
	Editorial findByNombre(String nombre);
	
	Editorial findByNombre(String nombre, Long id);
	
	Page<com.github.com.jorgdz.app.model.Editorial> findAllEditorial(Pageable pageable);
	
	Editorial save (Editorial editorial);
	
	void deleteById(Long id);
}
