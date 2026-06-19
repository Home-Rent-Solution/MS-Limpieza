package com.HomeRentSolution.ms_limpieza.assemblers;

import com.HomeRentSolution.ms_limpieza.controller.LimpiezaV2Controller;
import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class LimpiezaAssembler extends RepresentationModelAssemblerSupport<Limpieza, LimpiezaResponseDTO> {

    public LimpiezaAssembler() {
        super(LimpiezaV2Controller.class, LimpiezaResponseDTO.class);
    }

    @Override
    public LimpiezaResponseDTO toModel(Limpieza entidad) {
        LimpiezaResponseDTO dto = new LimpiezaResponseDTO();
        dto.setIdLimpieza(entidad.getIdLimpieza());
        dto.setIdReserva(entidad.getIdReserva());
        dto.setIdPropiedad(entidad.getIdPropiedad());
        dto.setFechaProgramada(entidad.getFechaProgramada());
        dto.setFechaRealizada(entidad.getFechaRealizada());
        dto.setEstadoLimpieza(entidad.getEstadoLimpieza());
        dto.setObservaciones(entidad.getMotivo());

        dto.add(linkTo(methodOn(LimpiezaV2Controller.class)
                .obtenerPorId(entidad.getIdLimpieza())).withSelfRel());
        return dto;
    }

}
