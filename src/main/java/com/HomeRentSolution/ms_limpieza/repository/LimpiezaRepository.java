package com.HomeRentSolution.ms_limpieza.repository;

import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LimpiezaRepository extends JpaRepository<Limpieza, Long> {

    List<Limpieza> findBybuscarPorId(Long idLimpieza);
    List<Limpieza> findByEstadoLimpieza(EstadoLimpieza estadoLimpieza);

}
