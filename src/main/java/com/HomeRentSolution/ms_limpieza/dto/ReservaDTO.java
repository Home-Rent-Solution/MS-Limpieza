package com.HomeRentSolution.ms_limpieza.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ReservaDTO {

    private Long idReserva;
    private Long idPropiedad;
    private Long idInquilino;
    private BigDecimal montoTotal;
    private LocalDateTime fechaVencimiento;
}
