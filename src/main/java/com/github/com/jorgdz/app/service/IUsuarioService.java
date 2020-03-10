package com.github.com.jorgdz.app.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.com.jorgdz.app.entity.Usuario;

public interface IUsuarioService {
	
	Usuario findById(Long id);
	
	Page<Usuario> findAll(Optional<String> nombres, Optional<String> apellidos, Optional<String> correo, Optional<Boolean> enabled, Pageable pageable);
	
	Usuario findByCorreo(String correo);
	
	Usuario findByCorreo(String correo, Long id);
	
	Usuario save(Usuario usuario);
}
