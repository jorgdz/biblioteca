package com.github.com.jorgdz.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.com.jorgdz.app.entity.Editorial;

@Repository
public interface EditorialRepo extends JpaRepository<Editorial, Long>{
	
	//Editorial findByNombre(String nombre);
	
	@Query("SELECT new com.github.com.jorgdz.app.model.Editorial(e.id, e.nombre) FROM Editorial e")
	Page<com.github.com.jorgdz.app.model.Editorial> findAllEditorial (Pageable pageable);
	
}
