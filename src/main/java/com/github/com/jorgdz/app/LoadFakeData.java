package com.github.com.jorgdz.app;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.github.com.jorgdz.app.entity.Autor;
import com.github.com.jorgdz.app.entity.Editorial;
import com.github.com.jorgdz.app.entity.Libro;
import com.github.com.jorgdz.app.entity.Permiso;
import com.github.com.jorgdz.app.entity.Rol;
import com.github.com.jorgdz.app.entity.Usuario;
import com.github.com.jorgdz.app.repository.AutorRepo;
import com.github.com.jorgdz.app.repository.EditorialRepo;
import com.github.com.jorgdz.app.repository.LibroRepo;
import com.github.com.jorgdz.app.repository.PermisoRepo;
import com.github.com.jorgdz.app.repository.RolRepo;
import com.github.com.jorgdz.app.repository.UsuarioRepo;
import com.github.com.jorgdz.app.util.AppHelper;
import com.github.javafaker.Faker;

//@Component
public class LoadFakeData /*implements ApplicationListener<ContextRefreshedEvent>*/{
	
	@Autowired
	private AutorRepo autorRepo;
	
	@Autowired
	private UsuarioRepo userRepo;
	
	@Autowired
	private RolRepo rolRepo;
	
	@Autowired
	private LibroRepo libroRepo;
	
	@Autowired
	private EditorialRepo editorialRepo;
	
	@Autowired 
	private PermisoRepo permisoRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	 
	//@Override
	public void onApplicationEvent(ContextRefreshedEvent event) 
	{	
		Collection<Permiso> permisos = Arrays.asList(new Permiso("ROLES_WITH_USERS_PERMISSIONS", AppHelper.PREFIX.concat("/roles")), 
				new Permiso("ROLE_BY_ID_WITH_USERS_PERMISSIONS", AppHelper.PREFIX.concat("/roles/**")),
				new Permiso("ROL_SIMPLE", AppHelper.PREFIX.concat("/rol")),
				new Permiso("ROL_SIMPLE_BY_ID", AppHelper.PREFIX.concat("/rol/**")),
				new Permiso("CREATE_ROLES", AppHelper.PREFIX.concat("/roles")),
				new Permiso("UPDATE_ROLES", AppHelper.PREFIX.concat("/roles/**")),
				new Permiso("DELETE_ROLES", AppHelper.PREFIX.concat("roles/**")),
				new Permiso("PERMISSIONS", AppHelper.PREFIX.concat("/permisos")),
				new Permiso("PERMISSION_BY_ID", AppHelper.PREFIX.concat("/permisos/**")),
				new Permiso("EDITORIALES_WITH_LIBROS", AppHelper.PREFIX.concat("/editorial/libros")),
				new Permiso("EDITORIALES", AppHelper.PREFIX.concat("/editoriales")),	
				new Permiso("LIBROS", AppHelper.PREFIX.concat("/libros")),
				new Permiso("USUARIOS", AppHelper.PREFIX.concat("/usuarios")),
				new Permiso("USUARIOS_BY_ID", AppHelper.PREFIX.concat("/usuarios/**")),
				new Permiso("CREATE_USUARIOS", AppHelper.PREFIX.concat("/usuarios")));
		
		permisoRepo.saveAll(permisos);
		
		Rol rol1 = new Rol();
		rol1.setNombre("ADMIN");
		rol1.setPermisos(permisos);
		
		Rol rol2 = new Rol();
		rol2.setNombre("USUARIO");
		
		Collection<Rol> roles = Arrays.asList(rol1, rol2);
		rolRepo.saveAll(roles);
				
		Set<Rol> roleUser = new HashSet<Rol>();
		roleUser.add(rol1);
		
		//$2a$10$HlFUDBXs9EkVq8yXiQ5nYeHr.Nc0Ej4ATzdXx9n7kVAmqY5TyxK2q -> test
		Usuario u1 = new Usuario();
		u1.setNombres("Jorge Isrrael");
		u1.setApellidos("Diaz Montoya");
		u1.setClave(passwordEncoder.encode("admin"));
		u1.setCorreo("jdzm@outlook.es");
		u1.setEnabled(true);
		u1.setRoles(roleUser);
		userRepo.save(u1);
		
		Usuario u2 = new Usuario();
		u2.setNombres("Josué Aron");
		u2.setApellidos("Caballero Macías");
		u2.setClave(passwordEncoder.encode("12345"));
		u2.setCorreo("elsorbo@gmail.com");
		u2.setEnabled(true);
		userRepo.save(u2);
		
		Usuario u3 = new Usuario();
		u3.setNombres("Cesar");
		u3.setApellidos("Lata");
		u3.setClave(passwordEncoder.encode("12345"));
		u3.setCorreo("cess15@gmail.com");
		u3.setEnabled(true);
		userRepo.save(u3);
		
		Usuario u4 = new Usuario();
		u4.setNombres("Joel");
		u4.setApellidos("Velez");
		u4.setClave(passwordEncoder.encode("12345"));
		u4.setCorreo("jvlz@live.com");
		u4.setEnabled(false);
		userRepo.save(u4);
		
		Faker faker = new Faker();
		
		for (int i = 0; i < 25; i++) 
		{
			Editorial editorial = new Editorial();
			editorial.setNombre(faker.book().genre() + "" + (i+1));
			editorialRepo.save(editorial);
		}
				
		for (int i = 0; i < 200; i++) 
		{
			int id = (int) (Math.random() * 25 + 1);
			
			Editorial editorial = editorialRepo.findById((long) id).orElse(null);
			
			Libro libro = new Libro();
			libro.setIsbn("IS" + (i+1) + "" + faker.number().randomDigit());
			libro.setTitulo(faker.book().title());
			libro.setUsuario(u1);
			libro.setSinopsis(faker.lorem().paragraph(1));
			libro.setEditorial(editorial);
			
			Set<Autor> autores = new HashSet<Autor>();
			
			int cant = (int) (Math.random() * 4 + 1);
			
			for (int j = 0; j < cant; j++) 
			{
				Autor autor = new Autor();
				autor.setNombres(faker.name().name());
				autor.setApellidos(faker.name().lastName());
				autorRepo.save(autor);
				autores.add(autor);
			}
			
	        libro.setAutores(autores);
	        libroRepo.save(libro);
		}
	}

}
