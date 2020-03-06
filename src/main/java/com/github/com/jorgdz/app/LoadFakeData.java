package com.github.com.jorgdz.app;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;

import com.github.com.jorgdz.app.entity.Autor;
import com.github.com.jorgdz.app.entity.Editorial;
import com.github.com.jorgdz.app.entity.Libro;
import com.github.com.jorgdz.app.entity.Rol;
import com.github.com.jorgdz.app.entity.Usuario;
import com.github.com.jorgdz.app.repository.AutorRepo;
import com.github.com.jorgdz.app.repository.EditorialRepo;
import com.github.com.jorgdz.app.repository.LibroRepo;
import com.github.com.jorgdz.app.repository.RolRepo;
import com.github.com.jorgdz.app.repository.UsuarioRepo;
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
	
	//@Override
	@SuppressWarnings("unchecked")
	public void onApplicationEvent(ContextRefreshedEvent event) 
	{
		Usuario u1 = new Usuario();
		u1.setNombres("Jorge Isrrael");
		u1.setApellidos("Diaz Montoya");
		u1.setClave("$2a$10$HlFUDBXs9EkVq8yXiQ5nYeHr.Nc0Ej4ATzdXx9n7kVAmqY5TyxK2q");
		u1.setCorreo("jdzm@outlook.es");
		u1.setEnabled(true);
		
		Rol rol1 = new Rol();
		rol1.setNombre("ROLE_ADMIN");
		
		Rol rol2 = new Rol();
		rol2.setNombre("ROLE_USUARIO");
		
		Collection<Rol> roles = Arrays.asList(rol1, rol2);
		rolRepo.saveAll(roles);
		
		u1.setRoles((Set<Rol>) Arrays.asList(rol1));
		userRepo.save(u1);
		

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
