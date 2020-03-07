package com.github.com.jorgdz.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "roles")
public class Rol implements Serializable{

	private static final long serialVersionUID = 3680074935443116666L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@Column(name = "nombre", unique = true)
	private String nombre;
	
	
	@ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"roles", "libros"})
	private Set<Usuario> usuarios;
	
	@ManyToMany
	@JoinTable(name = "permisos_roles", joinColumns = @JoinColumn(name = "rol_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "permiso_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"roles"})
	private Collection<Permiso> permisos;
	
	
	public Rol(Long id, @NotEmpty String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}

	public Rol() 
	{
		this.permisos = new ArrayList<Permiso>();
	}

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

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public Collection<Permiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(Collection<Permiso> permisos) {
		this.permisos = permisos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rol other = (Rol) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Rol [id=" + id + ", nombre=" + nombre + ", usuarios=" + usuarios + "]";
	}
	
}
