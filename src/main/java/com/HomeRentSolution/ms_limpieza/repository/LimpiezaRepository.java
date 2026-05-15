package com.HomeRentSolution.ms_limpieza.repository;

import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LimpiezaRepository extends JpaRepository<Limpieza, Long> {

    List<Limpieza> findByIdLimpieza(Long idLimpieza);
    List<Limpieza> findByIdReserva(Long idReserva);
    List<Limpieza> findByIdPropiedad(Long idPropiedad);
}
