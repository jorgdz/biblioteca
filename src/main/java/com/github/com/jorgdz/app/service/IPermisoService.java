package com.github.com.jorgdz.app.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.com.jorgdz.app.entity.Permiso;

public interface IPermisoService {

	Page<Permiso> findAll (Optional<String> nombre, Pageable pageable);
	
	Permiso findById(Long id);
	
	void deletePermisoRolById(Long rol_id, Long permiso_id);
	
}
