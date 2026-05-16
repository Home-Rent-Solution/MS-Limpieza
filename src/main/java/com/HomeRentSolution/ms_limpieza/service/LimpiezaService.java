package com.HomeRentSolution.ms_limpieza.service;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.repository.LimpiezaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LimpiezaService {

    private final LimpiezaRepository limpiezaRepository;

    public LimpiezaResponseDTO buscarPorId(Long id) {

        Limpieza limpieza = limpiezaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        LimpiezaResponseDTO dto = new LimpiezaResponseDTO();
        dto.setIdLimpieza(limpieza.getIdLimpieza());


        return dto;
    }


    public List<Limpieza> buscarPorEstado(EstadoLimpieza estadoLimpieza) {
        return limpiezaRepository.findByEstadoLimpieza(estadoLimpieza);
    }


    public List<LimpiezaResponseDTO> buscarTodas() { return limpiezaRepository.findAll()
            .stream()
            .map(limpieza -> {
                LimpiezaResponseDTO dto = new LimpiezaResponseDTO();
                dto.setIdLimpieza(limpieza.getIdLimpieza());

                return dto;
            })
            .toList(); }


    public LimpiezaResponseDTO agendarLimpieza(ReservaDTO request) {
        Limpieza nuevaLimpieza = new Limpieza();

        nuevaLimpieza.setIdReserva(request.getIdReserva());
        nuevaLimpieza.setIdPropiedad(request.getIdPropiedad());
        nuevaLimpieza.setFechaProgramada(request.getFechaFin());
        nuevaLimpieza.setEstadoLimpieza(EstadoLimpieza.PENDIENTE);

        Limpieza entidadGuardada = limpiezaRepository.save(nuevaLimpieza);

        LimpiezaResponseDTO response = new LimpiezaResponseDTO();
        response.setIdLimpieza(entidadGuardada.getIdLimpieza());
        response.setIdReserva(entidadGuardada.getIdReserva());
        response.setIdPropiedad(entidadGuardada.getIdPropiedad());
        response.setEstadoLimpieza(entidadGuardada.getEstadoLimpieza());
        response.setFechaProgramada(entidadGuardada.getFechaProgramada());

        return response;

    }

    public LimpiezaResponseDTO cambiarEstado(Long idLimpieza, EstadoLimpieza nuevoEstado) {
        Limpieza limpieza = limpiezaRepository.findById(idLimpieza)
                .orElseThrow(() -> new RuntimeException("Limpieza no encontrada"));

        // Validar transiciones permitidas
        if (!transicionPermitida(limpieza.getEstadoLimpieza(), nuevoEstado)) {
            throw new RuntimeException("No se puede pasar de " + limpieza.getEstadoLimpieza() + " a " + nuevoEstado);
        }

        limpieza.setEstadoLimpieza(nuevoEstado);
        limpiezaRepository.save(limpieza);

        LimpiezaResponseDTO dto = new LimpiezaResponseDTO();
        dto.setIdLimpieza(limpieza.getIdLimpieza());


        return dto;


    }

    private boolean transicionPermitida(EstadoLimpieza actual, EstadoLimpieza nuevo) {
        // Reglas de negocio
        return switch (actual) {
            case PENDIENTE -> nuevo == EstadoLimpieza.EN_PROCESO ||
                    nuevo == EstadoLimpieza.CANCELADA_POR_SISTEMA ||
                    nuevo == EstadoLimpieza.CANCELADA_POR_PERSONAL;

            case EN_PROCESO -> nuevo == EstadoLimpieza.COMPLETADA ||
                    nuevo == EstadoLimpieza.CANCELADA_POR_PERSONAL;

            default -> false;  // COMPLETADA o CANCELADA no pueden cambiar
        };
    }

    private LimpiezaResponseDTO ejecutarCancelacion(
            Long idLimpieza,
            EstadoLimpieza estadoDestino,
            String observaciones) {

        Limpieza limpieza = limpiezaRepository.findById(idLimpieza)
                .orElseThrow(() -> new RuntimeException("Limpieza no encontrada con id: " + idLimpieza));

        if (limpieza.getEstadoLimpieza() != EstadoLimpieza.PENDIENTE) {
            throw new RuntimeException(
                    "Solo se puede cancelar una limpieza en estado PENDIENTE. Estado actual: "
                            + limpieza.getEstadoLimpieza()
            );
        }

        limpieza.setEstadoLimpieza(estadoDestino);
        limpieza.setMotivo(observaciones); // tu entidad usa "motivo", no "observaciones"

        Limpieza guardada = limpiezaRepository.save(limpieza);
        return toResponseDTO(guardada);
    }

    // Llamado por ms-reservas automáticamente al cancelar una reserva
    public LimpiezaResponseDTO cancelarPorSistema(Long idLimpieza, String observaciones) {
        return ejecutarCancelacion(idLimpieza, EstadoLimpieza.CANCELADA_POR_SISTEMA, observaciones);
    }

    // Llamado por el personal de aseo directamente
    public LimpiezaResponseDTO cancelarPorPersonal(Long idLimpieza, String observaciones) {
        return ejecutarCancelacion(idLimpieza, EstadoLimpieza.CANCELADA_POR_PERSONAL, observaciones);
    }

    // Este es el que ya tenías — lo mantienes para cancelar-terreno del controller
    public LimpiezaResponseDTO cancelarLimpieza(
            Long idLimpieza,
            EstadoLimpieza estadoLimpieza,
            String observaciones) {
        return ejecutarCancelacion(idLimpieza, estadoLimpieza, observaciones);
    }

    public LimpiezaResponseDTO toResponseDTO(Limpieza limpieza) {
        LimpiezaResponseDTO dto = new LimpiezaResponseDTO();
        dto.setIdLimpieza(limpieza.getIdLimpieza());
        dto.setIdPropiedad(limpieza.getIdPropiedad());
        dto.setIdReserva(limpieza.getIdReserva());
        dto.setFechaProgramada(limpieza.getFechaProgramada());
        dto.setFechaRealizada(limpieza.getFechaRealizada());
        dto.setEstadoLimpieza(limpieza.getEstadoLimpieza());
        dto.setObservaciones(limpieza.getMotivo()); // en entidad es "motivo", en DTO es "observaciones"
        return dto;
    }





}

