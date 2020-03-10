package com.github.com.jorgdz.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.com.jorgdz.app.entity.Editorial;
import com.github.com.jorgdz.app.repository.EditorialRepo;

@Service
public class EditorialService implements IEditorialService{
	
	@Autowired
	private EditorialRepo repoEditorial;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Transactional(readOnly = true)
	@Override
	public Page<Editorial> findAll(Pageable pageable) 
	{
		return repoEditorial.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Page<com.github.com.jorgdz.app.model.Editorial> findAllEditorial(Pageable pageable) 
	{
		return repoEditorial.findAllEditorial(pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public Editorial findById(Long id) 
	{
		return repoEditorial.findById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	@Override
	public Editorial findByNombre(String nombre) 
	{
		return repoEditorial.findByNombre(nombre);
	}

	@Transactional(readOnly = true)
	@Override
	public Editorial findByNombre(String nombre, Long id) 
	{
		return repoEditorial.findByNombre(nombre, id);
	}

	@Transactional
	@Override
	public Editorial save(Editorial editorial) 
	{
		return repoEditorial.save(editorial);
	}
	
	@Transactional
	@Override
	public void deleteById(Long id) 
	{
		try 
		{
			repoEditorial.deleteById(id);
		} catch (Exception e) 
		{
			log.error("ERROR EN EditorialService al borrar: " + e.getMessage());
		}
	}
	
}
