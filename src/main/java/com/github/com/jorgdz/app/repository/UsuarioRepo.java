package com.github.com.jorgdz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.com.jorgdz.app.entity.Usuario;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Long>{

	@Modifying
	@Query(value = "DELETE FROM roles_usuarios WHERE rol_id=:rol_id", nativeQuery = true)
	void deleteUsuarioRolById (@Param("rol_id") Long rol_id);
	
}
