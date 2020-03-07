package com.github.com.jorgdz.app.service;

import org.springframework.beans.factory.annotation.Autowired;
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

}
