package com.github.com.jorgdz.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.com.jorgdz.app.entity.Editorial;
import com.github.com.jorgdz.app.repository.EditorialRepo;

@Service
public class EditorialService implements IEditorialService{
	
	@Autowired
	private EditorialRepo repoEditorial;

	@Override
	public Page<Editorial> findAll(Pageable pageable) 
	{
		return repoEditorial.findAll(pageable);
	}

	@Override
	public Page<Editorial> findAllEditorial(Pageable pageable) {
		return repoEditorial.findAllEditorial(pageable);
	}
	
}
