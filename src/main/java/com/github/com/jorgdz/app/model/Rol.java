package com.github.com.jorgdz.app.model;

public class Rol {
	
	private Long id;
	
	private String nombre;

	public Rol(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
	public Rol () {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
