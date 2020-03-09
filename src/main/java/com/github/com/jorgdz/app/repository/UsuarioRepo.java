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
	
	@Query("SELECT u FROM Usuario u WHERE LOWER(u.nombres) like %?1%")
	Page<Usuario> findAllByNombre (String nombre, Pageable pageable);
	
	@Query("SELECT u FROM Usuario u WHERE LOWER(u.apellidos) like %?1%")
	Page<Usuario> findAllByApellido (String apellidos, Pageable pageable);
	
	@Query("SELECT u FROM Usuario u WHERE LOWER(u.correo) like %?1%")
	Page<Usuario> findAllByCorreo (String correo, Pageable pageable);
	
	@Query("SELECT u FROM Usuario u WHERE u.enabled = ?1")
	Page<Usuario> findAllByEnabled (boolean enabled, Pageable pageable);
	
	@Query("SELECT u FROM Usuario u WHERE LOWER(u.nombres) = ?1 AND LOWER(u.apellidos) = ?2")
	Page<Usuario> findAllByNombreAndApellido (String nombre, String apellido, Pageable pageable);
	
	@Query("SELECT u FROM Usuario u WHERE LOWER(u.nombres) like %?1% AND LOWER(u.apellidos) like %?2% AND LOWER(u.correo) like %?3%")
	Page<Usuario> findAllByNombreAndApellidoAndCorreo (String nombre, String apellido, String correo, Pageable pageable);
	
	@Query("SELECT u FROM Usuario u WHERE LOWER(u.nombres) like %?1% AND LOWER(u.apellidos) like %?2% AND LOWER(u.correo) like %?3% AND u.enabled = ?4")
	Page<Usuario> findAllByNombreAndApellidoAndCorreoAndEnabled (String nombre, String apellido, String correo, boolean enabled, Pageable pageable);
	
	
	
	
	Usuario findByCorreo(String correo);
	
	@Modifying
	@Query(value = "DELETE FROM roles_usuarios WHERE rol_id=:rol_id", nativeQuery = true)
	void deleteUsuarioRolById (@Param("rol_id") Long rol_id);
	
}
