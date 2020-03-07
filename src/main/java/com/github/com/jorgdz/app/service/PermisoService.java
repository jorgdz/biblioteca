package com.github.com.jorgdz.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.com.jorgdz.app.entity.Permiso;
import com.github.com.jorgdz.app.repository.PermisoRepo;

@Service
public class PermisoService implements IPermisoService{

	@Autowired
	private PermisoRepo repoPermiso;
	
	@Transactional(readOnly = true)
	@Override
	public Permiso findById(Long id) {
		return repoPermiso.findById(id).orElse(null);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Page<Permiso> findAll(Optional<String> nombre, Pageable pageable) {
		return repoPermiso.findAllByNombre(nombre.orElse("_"), pageable);
	}
	
	@Transactional
	@Override
	public void deletePermisoRolById(Long rol_id, Long permiso_id) 
	{
		repoPermiso.deletePermisoRolById(rol_id, permiso_id);
	}

}
