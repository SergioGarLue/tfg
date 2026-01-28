package com.daw.tfg.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.daw.tfg.models.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long>{

}
