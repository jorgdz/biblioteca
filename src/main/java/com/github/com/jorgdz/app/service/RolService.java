package com.github.com.jorgdz.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.com.jorgdz.app.entity.Rol;
import com.github.com.jorgdz.app.repository.RolRepo;

@Service
public class RolService implements IRolService{

	@Autowired
	private RolRepo rolRepo;
	
	@Override
	public Page<Rol> findAllRoles(Pageable pageable) 
	{
		return rolRepo.findAllRoles(pageable);
	}
	
	@Override
	public Page<Rol> findAll(Pageable pageable) {
		return rolRepo.findAll(pageable);
	}

	@Override
	public Rol findByNombre(Optional<String> nombre) 
	{
		return rolRepo.findByName(nombre);
	}

	@Override
	public Rol findById(Long id) {
		return rolRepo.findById(id).orElse(null);
	}

	@Override
	public void save(Rol rol) {
		rolRepo.save(rol);
	}

	@Override
	public void delete(Long id) {
		rolRepo.deleteById(id);
	}

}
