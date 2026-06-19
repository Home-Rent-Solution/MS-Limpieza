package com.HomeRentSolution.ms_limpieza.controller;

import com.HomeRentSolution.ms_limpieza.assemblers.LimpiezaAssembler;
import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.service.LimpiezaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LimpiezaControllerTest {

    @Mock
    private LimpiezaService limpiezaService;

    @InjectMocks
    private LimpiezaController limpiezaController;

    private Limpieza limpieza;
    private LimpiezaResponseDTO limpiezaResponseDTO;
    private ReservaDTO reservaDTO;

    @BeforeEach
    void setUp() {
        limpieza = new Limpieza();
        limpieza.setIdLimpieza(1L);
        limpieza.setIdReserva(1L);
        limpieza.setIdPropiedad(2L);
        limpieza.setEstadoLimpieza(EstadoLimpieza.PENDIENTE);
        limpieza.setFechaProgramada(LocalDateTime.now().plusDays(3));
        limpieza.setFechaRealizada(null);
        limpieza.setMotivo(null);

        limpiezaResponseDTO = new LimpiezaResponseDTO();
        limpiezaResponseDTO.setIdLimpieza(1L);
        limpiezaResponseDTO.setIdReserva(1L);
        limpiezaResponseDTO.setIdPropiedad(2L);
        limpiezaResponseDTO.setEstadoLimpieza(EstadoLimpieza.PENDIENTE);
        limpiezaResponseDTO.setFechaProgramada(LocalDateTime.now().plusDays(3));

        reservaDTO = new ReservaDTO();
        reservaDTO.setIdReserva(1L);
        reservaDTO.setIdPropiedad(2L);
        reservaDTO.setIdInquilino(3L);
        reservaDTO.setFechaVencimiento(LocalDateTime.now().plusDays(3));
    }

    // PRUEBA 1: crear limpieza devuelve 201 CREATED con body
    @Test
    void crearLimpieza_debeRetornar201ConBody() {
        when(limpiezaService.agendarLimpieza(any(ReservaDTO.class)))
                .thenReturn(limpiezaResponseDTO);

        ResponseEntity<LimpiezaResponseDTO> respuesta =
                limpiezaController.crearLimpieza(reservaDTO);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(EstadoLimpieza.PENDIENTE, respuesta.getBody().getEstadoLimpieza());
        verify(limpiezaService, times(1)).agendarLimpieza(any(ReservaDTO.class));
    }

    // PRUEBA 2: obtener limpieza por ID devuelve DTO completo
    @Test
    void obtenerPorId_debeRetornarLimpiezaCompleta() {
        when(limpiezaService.obtenerEntidadPorId(1L)).thenReturn(limpieza);

        ResponseEntity<LimpiezaResponseDTO> respuesta =
                limpiezaController.obtenerPorId(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(1L, respuesta.getBody().getIdLimpieza());
        assertEquals(EstadoLimpieza.PENDIENTE, respuesta.getBody().getEstadoLimpieza());
    }

    // PRUEBA 3: obtener limpiezas por estado devuelve lista
    @Test
    void obtenerPorEstado_debeRetornarLista() {
        when(limpiezaService.obtenerPorEstado(EstadoLimpieza.PENDIENTE))
                .thenReturn(List.of(limpieza));

        ResponseEntity<List<LimpiezaResponseDTO>> respuesta =
                limpiezaController.obtenerPorEstado(EstadoLimpieza.PENDIENTE);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertFalse(respuesta.getBody().isEmpty());
        assertEquals(1, respuesta.getBody().size());
    }

    // PRUEBA 4: actualizar estado devuelve la limpieza actualizada
    @Test
    void actualizarEstado_debeRetornarLimpiezaActualizada() {
        Limpieza limpiezaActualizada = new Limpieza();
        limpiezaActualizada.setIdLimpieza(1L);
        limpiezaActualizada.setIdReserva(1L);
        limpiezaActualizada.setIdPropiedad(2L);
        limpiezaActualizada.setEstadoLimpieza(EstadoLimpieza.EN_PROCESO);
        limpiezaActualizada.setFechaProgramada(LocalDateTime.now().plusDays(3));

        when(limpiezaService.cambiarEstado(1L, EstadoLimpieza.EN_PROCESO))
                .thenReturn(limpiezaActualizada);

        ResponseEntity<LimpiezaResponseDTO> respuesta =
                limpiezaController.actualizarEstado(1L, EstadoLimpieza.EN_PROCESO);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(EstadoLimpieza.EN_PROCESO, respuesta.getBody().getEstadoLimpieza());
        verify(limpiezaService, times(1))
                .cambiarEstado(1L, EstadoLimpieza.EN_PROCESO);
    }
}
