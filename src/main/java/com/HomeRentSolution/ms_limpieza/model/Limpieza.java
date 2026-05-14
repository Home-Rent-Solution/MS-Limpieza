package com.HomeRentSolution.ms_limpieza.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Data
public class Limpieza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLimpieza;

    private Long idPropiedad;
    private Long idReserva;

    private LocalDateTime fechaProgramada;  // cuando se agenda (fin de reserva)
    private LocalDateTime fechaRealizada;   // cuando se completó

    @Enumerated(EnumType.STRING)
    private EstadoLimpieza estado;
}
