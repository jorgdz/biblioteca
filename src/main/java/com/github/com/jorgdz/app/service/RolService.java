package com.github.com.jorgdz.app.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.com.jorgdz.app.entity.Rol;
import com.github.com.jorgdz.app.repository.PermisoRepo;
import com.github.com.jorgdz.app.repository.RolRepo;
import com.github.com.jorgdz.app.repository.UsuarioRepo;

@Service
public class RolService implements IRolService{

	@Autowired
	private RolRepo rolRepo;
	
	@Autowired
	private PermisoRepo permisoRepo;
	
	@Autowired
	private UsuarioRepo usuarioRepo;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Transactional(readOnly = true)
	@Override
	public Page<com.github.com.jorgdz.app.model.Rol> findAll(Optional<String> nombre, Pageable pageable) 
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

	@Transactional
	@Override
	public void delete(Long id) {
		try 
		{
			permisoRepo.deletePermisoRolById(id);
			usuarioRepo.deleteUsuarioRolById(id);
			rolRepo.deleteRolById(id);
		}
		catch (DataAccessException e) 
		{
			log.error(e.getMessage());
		}
	}

	@Override
	public void deleteRolUsuarioById(Long usuario_id, Long rol_id) 
	{
		this.rolRepo.deleteRolUsuarioById(usuario_id, rol_id);
	}

}
