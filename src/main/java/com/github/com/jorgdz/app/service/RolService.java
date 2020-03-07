package com.github.com.jorgdz.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.com.jorgdz.app.entity.Rol;
import com.github.com.jorgdz.app.repository.RolRepo;

@Service
public class RolService implements IRolService{

	@Autowired
	private RolRepo rolRepo;
	
	@Transactional(readOnly = true)
	@Override
	public Page<Rol> findAll(Optional<String> nombre, Pageable pageable) 
	{
		return rolRepo.findAllRoles(nombre.orElse("_").toUpperCase(), pageable);
	}
		
	@Transactional(readOnly = true)
	@Override
	public Page<Rol> findAllByNombre(Optional<String> nombre, Pageable pageable) {
		return rolRepo.findAllByNombre(nombre.orElse("_").toUpperCase(), pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public Rol findById(Long id) {
		return rolRepo.findById(id).orElse(null);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Rol findRolById(Long id) 
	{
		return rolRepo.findRolById(id).orElse(null);		
	}
	
	@Transactional(readOnly = true)
	@Override
	public Rol findByNombre(String nombre) {
		return rolRepo.findByNombre(nombre);
	}

	@Transactional(readOnly = true)
	@Override
	public Rol findByNombre(String nombre, Long id) {
		return rolRepo.findByNombre(nombre, id);
	}
	
	@Transactional
	@Override
	public Rol save(Rol rol) {
		return rolRepo.save(rol);
	}

	@Override
	public void delete(Long id) {
		rolRepo.deleteById(id);
	}

}
