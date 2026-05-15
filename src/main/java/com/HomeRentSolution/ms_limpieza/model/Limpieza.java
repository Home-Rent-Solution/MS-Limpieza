package com.HomeRentSolution.ms_limpieza.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
public class Limpieza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLimpieza;

    @Column(name = "id_propiedad", nullable = false)
    private Long idPropiedad;

    @Column(name = "id_reserva", nullable = false)
    private Long idReserva;

    @Column(nullable = false)
    private LocalDateTime fechaProgramada;  // cuando se agenda (fin de reserva)

    @Column(nullable = false)
    private LocalDateTime fechaRealizada;   // cuando se completó

    @Enumerated(EnumType.STRING)
    private EstadoLimpieza estadoLimpieza;

    @Column(nullable = false)
    private String observaciones;
}
