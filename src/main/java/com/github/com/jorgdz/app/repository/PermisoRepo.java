package com.github.com.jorgdz.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.com.jorgdz.app.entity.Permiso;

@Repository
public interface PermisoRepo extends JpaRepository<Permiso, Long>{

}
