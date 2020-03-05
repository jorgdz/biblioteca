package com.github.com.jorgdz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.com.jorgdz.app.entity.Autor;

@Repository
public interface AutorRepo extends JpaRepository<Autor, Long>{

}
