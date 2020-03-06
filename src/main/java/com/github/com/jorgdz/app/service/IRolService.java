package com.github.com.jorgdz.app.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.com.jorgdz.app.entity.Rol;

public interface IRolService {
	
	Page<Rol> findAllRoles(Pageable pageable);	
	
	Page<Rol> findAll(Pageable pageable);	
	
	Rol findByNombre(Optional<String> nombre);
	
	Rol findById(Long id);
	
	void save(Rol rol);
	
	void delete(Long id);
	
}
