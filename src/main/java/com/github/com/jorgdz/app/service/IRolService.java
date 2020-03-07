package com.github.com.jorgdz.app.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.com.jorgdz.app.entity.Rol;

public interface IRolService {
	
	Page<Rol> findAll(Optional<String> nombre, Pageable pageable);	
		
	Page<Rol> findAllByNombre(Optional<String> nombre, Pageable pageable);	
	
	Rol findById(Long id);
	
	Rol findRolById(Long id);
	
	Rol save(Rol rol);
	
	void delete(Long id);
	
}
