package com.github.com.jorgdz.app.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.com.jorgdz.app.entity.Rol;

public interface IRolService {
	
	Page<com.github.com.jorgdz.app.model.Rol> findAll(Optional<String> nombre, Pageable pageable);	
		
	Page<Rol> findAllByNombre(Optional<String> nombre, Pageable pageable);	
	
	Rol findById(Long id);
	
	Rol findRolById(Long id);
	
	Rol findByNombre(String nombre);
	
	Rol findByNombre(String nombre, Long id);
	
	Rol save(Rol rol);
	
	void delete(Long id);
	
	
	void deleteRolUsuarioById(Long usuario_id, Long rol_id);
}
