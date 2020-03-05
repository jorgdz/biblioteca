package com.github.com.jorgdz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.com.jorgdz.app.entity.Editorial;

@Repository
public interface EditorialRepo extends JpaRepository<Editorial, Long>{
	
	Editorial findByNombre(String nombre);
	
}
