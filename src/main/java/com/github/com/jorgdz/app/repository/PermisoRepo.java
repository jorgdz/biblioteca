package com.github.com.jorgdz.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.com.jorgdz.app.entity.Permiso;

@Repository
public interface PermisoRepo extends JpaRepository<Permiso, Long>{
	
	@Modifying
	@Query(value = "DELETE FROM permisos_roles WHERE rol_id=:rol_id and permiso_id=:permiso_id", nativeQuery = true)
	void deletePermisoRolById (@Param("rol_id") Long rol_id, @Param("permiso_id") Long permiso_id);
	
	@Query("SELECT p FROM Permiso p WHERE p.nombre like %?1%")
	Page<Permiso> findAllByNombre (String nombre, Pageable pageable);
}
