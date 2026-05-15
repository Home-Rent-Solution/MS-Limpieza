package com.HomeRentSolution.ms_limpieza.dto;

import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LimpiezaRequestDTO {

    private Long idPropiedad;
    private Long idReserva;
    private LocalDateTime fechaProgramada;
    private EstadoLimpieza estadoLimpieza;
}
