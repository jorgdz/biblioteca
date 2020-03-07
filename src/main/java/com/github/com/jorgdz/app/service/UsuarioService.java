package com.github.com.jorgdz.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.com.jorgdz.app.entity.Usuario;
import com.github.com.jorgdz.app.repository.UsuarioRepo;

@Service
public class UsuarioService implements IUsuarioService {
	
	@Autowired
	private UsuarioRepo repoUsuario;

	@Override
	public Usuario findById(Long id) {
		return repoUsuario.findById(id).orElse(null);
	}
	
	
}
