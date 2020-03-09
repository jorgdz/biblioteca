package com.github.com.jorgdz.app.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.com.jorgdz.app.entity.Permiso;
import com.github.com.jorgdz.app.entity.Rol;
import com.github.com.jorgdz.app.entity.Usuario;
import com.github.com.jorgdz.app.repository.UsuarioRepo;

@Transactional
@Service("userDetailService")
public class UserDetailService implements UserDetailsService{
	
	private Logger logger = LoggerFactory.getLogger(UserDetailService.class);
	
	@Autowired
	private UsuarioRepo repoUsuario;
		
	@Override
	public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException 
	{
		Usuario usuario = repoUsuario.findByCorreo(correo);
		
		if(usuario == null)
		{
			logger.error("Error login: no existe el usuario con correo: '" + correo + "'");
			throw new UsernameNotFoundException("No tenemos registros del correo " + correo);
		}
		
		return new User(usuario.getCorreo(), usuario.getClave(), usuario.isEnabled(), true, true, true, getAuthorities(usuario.getRoles()));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Set<Rol> roles)
	{
		return getGrantedAuthorities(getPermisos(roles));
	}
	
	private List<Permiso> getPermisos(Set<Rol> roles) 
	{
        List<Permiso> permisos = new ArrayList<Permiso>();
        
        for (Rol rol : roles) 
        {
        	logger.info(rol.toString());   	
        	permisos.addAll(rol.getPermisos());
        }
        
        return permisos;
    }
	
	private List<GrantedAuthority> getGrantedAuthorities(List<Permiso> permisos) 
	{
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (Permiso permiso : permisos) 
        {
        	authorities.add(new SimpleGrantedAuthority(permiso.getNombre()));
		}
        return authorities;
    }
}
