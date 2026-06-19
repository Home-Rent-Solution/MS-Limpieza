package com.HomeRentSolution.ms_limpieza.service;

import com.HomeRentSolution.ms_limpieza.config.AppConfig;
import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.exception.LimpiezaNoEncontradaException;
import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.repository.LimpiezaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LimpiezaService {

    private final LimpiezaRepository limpiezaRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional(readOnly = true)
    public Limpieza obtenerEntidadPorId(Long id) {
        return limpiezaRepository.findById(id)
                .orElseThrow(() -> new LimpiezaNoEncontradaException(id));
    }

    @Transactional(readOnly = true)
    public List<Limpieza> obtenerPorEstado(EstadoLimpieza estadoLimpieza) {
        return limpiezaRepository.findByEstadoLimpieza(estadoLimpieza);
    }

    @Transactional(readOnly = true)
    public List<Limpieza> obtenerTodas() {
        return limpiezaRepository.findAll();
    }

    @Transactional
    public LimpiezaResponseDTO agendarLimpieza(ReservaDTO request) {
        Limpieza nuevaLimpieza = new Limpieza();
        nuevaLimpieza.setIdReserva(request.getIdReserva());
        nuevaLimpieza.setIdPropiedad(request.getIdPropiedad());
        nuevaLimpieza.setFechaProgramada(request.getFechaVencimiento());
        nuevaLimpieza.setEstadoLimpieza(EstadoLimpieza.PENDIENTE);

        Limpieza entidadGuardada = limpiezaRepository.save(nuevaLimpieza);
        log.info("Limpieza agendada para propiedad ID: {}", request.getIdPropiedad());

        LimpiezaResponseDTO response = new LimpiezaResponseDTO();
        response.setIdLimpieza(entidadGuardada.getIdLimpieza());
        response.setEstadoLimpieza(entidadGuardada.getEstadoLimpieza());
        return response;
    }

    @Transactional
    public Limpieza cambiarEstado(Long idLimpieza, EstadoLimpieza nuevoEstado) {
        Limpieza limpieza = limpiezaRepository.findById(idLimpieza)
                .orElseThrow(() -> new LimpiezaNoEncontradaException(idLimpieza));

        if (!transicionPermitida(limpieza.getEstadoLimpieza(), nuevoEstado)) {
            throw new IllegalArgumentException("Transición inválida: No se puede pasar de "
                    + limpieza.getEstadoLimpieza() + " a " + nuevoEstado);
        }

        limpieza.setEstadoLimpieza(nuevoEstado);
        Limpieza guardada = limpiezaRepository.save(limpieza);

        rabbitTemplate.convertAndSend(AppConfig.LIMPIEZAS_EXCHANGE,
                AppConfig.ROUTING_ESTADO_CAMBIADO, guardada);
        log.info("[RabbitMQ] Evento enviado. Limpieza ID {} cambió a {}",
                idLimpieza, nuevoEstado);
        return guardada;
    }

    private boolean transicionPermitida(EstadoLimpieza actual, EstadoLimpieza nuevo) {
        return switch (actual) {
            case PENDIENTE -> nuevo == EstadoLimpieza.EN_PROCESO
                    || nuevo == EstadoLimpieza.CANCELADA_POR_SISTEMA
                    || nuevo == EstadoLimpieza.CANCELADA_POR_PERSONAL;
            case EN_PROCESO -> nuevo == EstadoLimpieza.COMPLETADA
                    || nuevo == EstadoLimpieza.CANCELADA_POR_PERSONAL;
            default -> false;
        };
    }

    private Limpieza ejecutarCancelacion(Long idLimpieza,
                                         EstadoLimpieza estadoDestino,
                                         String observaciones) {
        Limpieza limpieza = limpiezaRepository.findById(idLimpieza)
                .orElseThrow(() -> new LimpiezaNoEncontradaException(idLimpieza));

        if (limpieza.getEstadoLimpieza() != EstadoLimpieza.PENDIENTE) {
            throw new IllegalArgumentException(
                    "Solo se puede cancelar una limpieza en estado PENDIENTE. Estado actual: "
                            + limpieza.getEstadoLimpieza());
        }
        limpieza.setEstadoLimpieza(estadoDestino);
        limpieza.setMotivo(observaciones);
        return limpiezaRepository.save(limpieza);
    }

    @Transactional
    public Limpieza cancelarPorSistema(Long idLimpieza, String observaciones) {
        return ejecutarCancelacion(idLimpieza,
                EstadoLimpieza.CANCELADA_POR_SISTEMA, observaciones);
    }

    @Transactional
    public Limpieza cancelarPorPersonal(Long idLimpieza, String observaciones) {
        return ejecutarCancelacion(idLimpieza,
                EstadoLimpieza.CANCELADA_POR_PERSONAL, observaciones);
    }

}

