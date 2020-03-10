package com.github.com.jorgdz.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.com.jorgdz.app.entity.Usuario;
import com.github.com.jorgdz.app.repository.UsuarioRepo;

@Service
public class UsuarioService implements IUsuarioService {
	
	@Autowired
	private UsuarioRepo repoUsuario;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Transactional(readOnly = true)
	@Override
	public Page<Usuario> findAll(Optional<String> nombres, Optional<String> apellidos, Optional<String> correo, Optional<Boolean> enabled, Pageable pageable)
	{
		if(!nombres.isPresent() && !apellidos.isPresent() && !correo.isPresent() && !enabled.isPresent())
		{
			return repoUsuario.findAll(pageable);
		}
		else
		{
			//select * from usuarios where lower(nombres) like lower('%null%') or lower(apellidos) like ('%diaz%') or lower(correo) like lower('%null%') or enabled = 'false';
			return repoUsuario.findAllByNombreAndApellidoAndCorreoAndEnabled(nombres.orElse("null").toLowerCase(), apellidos.orElse("null").toLowerCase(), correo.orElse("null").toLowerCase(), enabled.orElse(null), pageable);
		}	
	}
	
	@Transactional(readOnly = true)
	@Override
	public Usuario findById(Long id) {
		return repoUsuario.findById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	@Override
	public Usuario findByCorreo(String correo) 
	{
		return repoUsuario.findByCorreo(correo);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Usuario findByCorreo(String correo, Long id) {
		return repoUsuario.findByCorreo(correo, id);
	}
	
	@Transactional
	@Override
	public Usuario save(Usuario usuario) 
	{
		usuario.setClave(passwordEncoder.encode(usuario.getClave()));
		return repoUsuario.save(usuario);
	}
	
}
