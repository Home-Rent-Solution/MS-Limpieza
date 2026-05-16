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
        return limpiezaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }


    public List<Limpieza> buscarPorEstado(EstadoLimpieza estadoLimpieza) {
        return limpiezaRepository.findByEstadoLimpieza(estadoLimpieza);
    }


    public List<LimpiezaResponseDTO> buscarTodas() { return limpiezaRepository.findAll(); }


    public LimpiezaResponseDTO crearLimpieza(ReservaDTO request) {
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

        return mapearResponse(limpieza);
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

    public LimpiezaResponseDTO cancelarLimpieza(Long idLimpieza, EstadoLimpieza estadoLimpieza, String observaciones ) {

        Limpieza limpieza = limpiezaRepository.findById(idLimpieza)
                .orElseThrow(() -> new RuntimeException("Limpieza no encontrada"));

        limpieza.setIdLimpieza(limpieza.getIdLimpieza());
        limpieza.setEstadoLimpieza(EstadoLimpieza.CANCELADA_POR_SISTEMA);
        limpieza.setMotivo(limpieza.getMotivo());

        Limpieza guardarCancelacion = limpiezaRepository.save(limpieza);

        if (reservaResponseDTO.getIdLimpieza() != null) {
            limpiezaClient.cancelarLimpieza(
                    reserva.getIdLimpieza(),
                    EstadoLimpieza.CANCELADA_POR_SISTEMA,  // ← El estado lo decide Reservas
                    "Reserva cancelada: " + motivo
            );
        }
        LimpiezaResponseDTO LResponse = new LimpiezaResponseDTO();

        LResponse.setIdLimpieza(guardarCancelacion.getIdLimpieza());
        LResponse.setEstadoLimpieza(guardarCancelacion.getEstadoLimpieza());
        LResponse.setObservaciones(observaciones);

        return LResponse;
    }

//              Service (lógica interna):
//
//crearLimpieza(propiedadId, fecha) → guardar en BD
//
//cancelarLimpieza(id) → cambiar estado a CANCELADA
//
//obtenerUltimaLimpieza(propiedadId) → consultar cuándo fue la última limpieza
//
//actualizarEstado(id, nuevoEstado) → cambiar estado



}

