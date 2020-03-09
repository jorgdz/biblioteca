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
	public Page<Usuario> findAllByNombre(Optional<String> nombre, Pageable pageable)
	{
		return repoUsuario.findAllByNombre(nombre.orElse("_"), pageable);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Page<Usuario> findAll(Pageable pageable)
	{
		/*if(nombre.isPresent())
		{
			// GET USERS FOR NAME
			return repoUsuario.findAllByNombre(nombre.orElse("_").toLowerCase(), pageable);
		}
		else if(apellido.isPresent())
		{
			// GET USERS FOR LASTNAME
			return repoUsuario.findAllByApellido(apellido.orElse("_").toLowerCase(), pageable);
		}
		else if(correo.isPresent())
		{
			// GET USERS FOR EMAIL
			return repoUsuario.findAllByCorreo(correo.orElse("_").toLowerCase(), pageable);
		}
		else if(enabled.isPresent())
		{
			// GET USERS FOR ENABLED (TRUE, FALSE)
			boolean active = true;
			
			if(enabled.get().equals("0"))
			{
				active = false;
			}
			else 
			{
				active = true;
			}		
			return repoUsuario.findAllByEnabled(active, pageable);
		}
		else if (nombre.isPresent() && apellido.isPresent())
		{
			return repoUsuario.findAllByNombreAndApellido(nombre.orElse("_").toLowerCase(), apellido.orElse("_").toLowerCase(), pageable);
		}
		else if(nombre.isPresent() && apellido.isPresent() && correo.isPresent())
		{
			return repoUsuario.findAllByNombreAndApellidoAndCorreo(nombre.orElse("_").toLowerCase(), apellido.orElse("_").toLowerCase(), correo.orElse("_").toLowerCase(), pageable);
		}
		else if(nombre.isPresent() && apellido.isPresent() && correo.isPresent() && enabled.isPresent())
		{
			boolean active = true;
			
			if(enabled.get().equals("0"))
			{
				active = false;
			}
			else 
			{
				active = true;
			}	
			return repoUsuario.findAllByNombreAndApellidoAndCorreoAndEnabled(nombre.orElse("_").toLowerCase(), apellido.orElse("_").toLowerCase(), correo.orElse("_").toLowerCase(), active, pageable);
		}
		else*/
		//{
			return repoUsuario.findAll(pageable);
		//}
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

	@Override
	public Usuario save(Usuario usuario) 
	{
		usuario.setClave(passwordEncoder.encode(usuario.getClave()));
		return repoUsuario.save(usuario);
	}
	
}
