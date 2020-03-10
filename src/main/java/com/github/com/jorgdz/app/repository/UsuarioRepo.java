package com.github.com.jorgdz.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.com.jorgdz.app.entity.Usuario;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Long>{
	
	@Query("SELECT u FROM Usuario u WHERE LOWER(u.nombres) like %?1% OR LOWER(u.apellidos) like %?2% OR LOWER(u.correo) like %?3% OR u.enabled = ?4")
	Page<Usuario> findAllByNombreAndApellidoAndCorreoAndEnabled (String nombre, String apellido, String correo, Boolean enabled, Pageable pageable);
	
	
	Usuario findByCorreo(String correo);
	
	@Query("SELECT u FROM Usuario u WHERE u.correo = ?1 and u.id <> ?2")
	Usuario findByCorreo(String correo, Long id);
	
	@Modifying
	@Query(value = "DELETE FROM roles_usuarios WHERE rol_id=:rol_id", nativeQuery = true)
	void deleteUsuarioRolById (@Param("rol_id") Long rol_id);
	
}
