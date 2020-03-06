package com.github.com.jorgdz.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.com.jorgdz.app.entity.Editorial;

@Repository
public interface EditorialRepo extends JpaRepository<Editorial, Long>{
	
	Editorial findByNombre(String nombre);
	
	@Query("SELECT new Editorial(e.id, e.nombre) FROM Editorial e")
	Page<Editorial> findAllEditorial (Pageable pageable);
	
	/*@Query("SELECT e FROM Editorial e LEFT JOIN FETCH e.libros l")
	Page<Editorial> findAllWithLibro(Pageable pageable);*/
}
