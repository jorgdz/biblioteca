package com.github.com.jorgdz.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.com.jorgdz.app.entity.Rol;

@Repository
public interface RolRepo extends JpaRepository<Rol, Long> {
	
	@Query(value = "SELECT r FROM Rol r LEFT JOIN FETCH r.permisos p WHERE r.nombre = ?1")
	Rol findByName(Optional<String> nombre);
	
	@Query("SELECT new Rol(r.id, r.nombre) FROM Rol r")
	Page<Rol> findAllRoles (Pageable pageable);

}
