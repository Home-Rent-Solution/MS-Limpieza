package com.HomeRentSolution.ms_limpieza.dto;

import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LimpiezaResponseDTO {
    private Long idLimpieza;
    private Long idPropiedad;
    private Long idReserva;
    private LocalDateTime fechaProgramada;
    private LocalDateTime fechaRealizada;
    private EstadoLimpieza estadoLimpieza;
    private String observaciones;
}
