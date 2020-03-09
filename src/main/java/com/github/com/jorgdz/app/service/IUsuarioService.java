package com.github.com.jorgdz.app.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.com.jorgdz.app.entity.Usuario;

public interface IUsuarioService {
	
	Usuario findById(Long id);
	
	Page<Usuario> findAllByNombre(Optional<String> nombre, Pageable pageable);
	
	Page<Usuario> findAll(Pageable pageable);
	
	Usuario findByCorreo(String correo);
	
	Usuario save(Usuario usuario);
}
