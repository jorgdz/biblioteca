package com.github.com.jorgdz.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.com.jorgdz.app.entity.Editorial;

public interface IEditorialService {
	
	Page<Editorial> findAll(Pageable pageable);
	
	Page<Editorial> findAllEditorial(Pageable pageable);
}
