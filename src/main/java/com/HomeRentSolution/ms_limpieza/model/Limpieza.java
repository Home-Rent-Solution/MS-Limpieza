package com.HomeRentSolution.ms_limpieza.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "limpiezas")
public class Limpieza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLimpieza;

    @Column(name = "id_propiedad", nullable = false)
    private Long idPropiedad;

    @Column(name = "id_reserva", nullable = false)
    private Long idReserva;

    @Column(nullable = false)
    private LocalDateTime fechaProgramada;

    @Column(nullable = true)
    private LocalDateTime fechaRealizada;

    @Enumerated(EnumType.STRING)
    private EstadoLimpieza estadoLimpieza;

    @Column(nullable = true)
    private String motivo;
}
