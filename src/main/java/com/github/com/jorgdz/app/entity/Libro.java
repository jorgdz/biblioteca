package com.github.com.jorgdz.app.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "libros")
public class Libro implements Serializable {

	private static final long serialVersionUID = -4100563645428850086L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "isbn")
	private String isbn; 
	
	private String titulo;
	
	private String sinopsis;
	
	@ManyToOne
	@JsonIgnoreProperties({"libros"})
	private Editorial editorial;
	
	
	@JsonProperty(access = Access.READ_ONLY)
	@ManyToOne
	@JsonIgnoreProperties({"libros", "roles"})
	private Usuario usuario;
	
	
	
	@ManyToMany
	@JoinTable(name = "autores_libro", joinColumns = @JoinColumn(name = "libro_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "autor_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"libros"})
	private Set<Autor> autores;
	
	
	public Libro() {}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getIsbn() {
		return isbn;
	}


	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public String getSinopsis() {
		return sinopsis;
	}


	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}


	public Editorial getEditorial() {
		return editorial;
	}


	public void setEditorial(Editorial editorial) {
		this.editorial = editorial;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public Set<Autor> getAutores() {
		return autores;
	}


	public void setAutores(Set<Autor> autores) {
		this.autores = autores;
	}
	
	
}
