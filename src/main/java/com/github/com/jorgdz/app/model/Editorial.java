package com.github.com.jorgdz.app.model;

public class Editorial {
	
	private Long id;
	
	private String nombre;
	

	public Editorial(Long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
	public Editorial(){}
	
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
