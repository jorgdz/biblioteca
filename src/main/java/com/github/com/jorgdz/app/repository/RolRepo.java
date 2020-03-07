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
	
	@Query("SELECT new com.github.com.jorgdz.app.model.Rol(r.id, r.nombre) FROM Rol r WHERE r.nombre like %?1%")
	Page<Rol> findAllRoles (String nombre, Pageable pageable);
	
	@Query("SELECT new Rol(r.id, r.nombre) FROM Rol r WHERE r.id = ?1")
	Optional<Rol> findRolById (Long id);
	
	@Query("SELECT r FROM Rol r WHERE r.nombre like %?1%")
	Page<Rol> findAllByNombre (String nombre, Pageable pageable);
	
}