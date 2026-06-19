package com.HomeRentSolution.ms_limpieza.dto;

import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
public class LimpiezaResponseDTO extends RepresentationModel<LimpiezaResponseDTO> {
    private Long idLimpieza;
    private Long idReserva;
    private Long idPropiedad;
    private LocalDateTime fechaProgramada;
    private LocalDateTime fechaRealizada;
    private EstadoLimpieza estadoLimpieza;
    private String observaciones;
}
