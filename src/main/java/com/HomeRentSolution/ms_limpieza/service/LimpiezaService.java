package com.HomeRentSolution.ms_limpieza.service;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.repository.LimpiezaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LimpiezaService {

    private final LimpiezaRepository limpiezaRepository;

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

    public void cancelarLimpieza(Long idLimpieza, String observaciones, EstadoLimpieza estadoLimpieza){

        Limpieza cancelacion = buscarIdLimpieza(idLimpieza);

        if (cancelacion.getEstadoLimpieza() == )

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

