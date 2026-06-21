package com.HomeRentSolution.ms_limpieza.assemblers;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.CollectionModel;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LimpiezaAssemblerTest {

    private final LimpiezaAssembler assembler = new LimpiezaAssembler();

    @Test
    void toModelMapeaCamposYAgregaSelfLink() {
        LocalDateTime fechaProgramada = LocalDateTime.of(2026, 6, 30, 11, 0);
        LocalDateTime fechaRealizada = LocalDateTime.of(2026, 6, 30, 12, 0);

        Limpieza limpieza = crearLimpieza(1L);
        limpieza.setFechaProgramada(fechaProgramada);
        limpieza.setFechaRealizada(fechaRealizada);
        limpieza.setMotivo("Limpieza completada sin observaciones");

        LimpiezaResponseDTO dto = assembler.toModel(limpieza);

        assertEquals(1L, dto.getIdLimpieza());
        assertEquals(10L, dto.getIdReserva());
        assertEquals(20L, dto.getIdPropiedad());
        assertEquals(fechaProgramada, dto.getFechaProgramada());
        assertEquals(fechaRealizada, dto.getFechaRealizada());
        assertEquals(EstadoLimpieza.PENDIENTE, dto.getEstadoLimpieza());
        assertEquals("Limpieza completada sin observaciones", dto.getObservaciones());
        assertTrue(dto.getLink("self").isPresent());
    }

    @Test
    void toCollectionModelRetornaColeccionConElementos() {
        CollectionModel<LimpiezaResponseDTO> resultado =
                assembler.toCollectionModel(List.of(crearLimpieza(1L), crearLimpieza(2L)));

        assertEquals(2, resultado.getContent().size());
    }

    private Limpieza crearLimpieza(Long id) {
        Limpieza limpieza = new Limpieza();
        limpieza.setIdLimpieza(id);
        limpieza.setIdReserva(10L);
        limpieza.setIdPropiedad(20L);
        limpieza.setFechaProgramada(LocalDateTime.of(2026, 6, 30, 11, 0));
        limpieza.setEstadoLimpieza(EstadoLimpieza.PENDIENTE);
        return limpieza;
    }
}