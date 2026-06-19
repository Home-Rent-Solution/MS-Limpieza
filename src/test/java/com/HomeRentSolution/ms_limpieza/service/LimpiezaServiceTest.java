package com.HomeRentSolution.ms_limpieza.service;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.exception.LimpiezaNoEncontradaException;
import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.repository.LimpiezaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LimpiezaServiceTest {

    // @Mock crea objetos falsos — no usan base de datos real
    @Mock
    private LimpiezaRepository limpiezaRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    // @InjectMocks inyecta los mocks en el servicio
    @InjectMocks
    private LimpiezaService limpiezaService;

    private ReservaDTO reservaDTO;
    private Limpieza limpieza;

    // @BeforeEach se ejecuta antes de cada prueba
    @BeforeEach
    void setUp() {
        reservaDTO = new ReservaDTO();
        reservaDTO.setIdReserva(1L);
        reservaDTO.setIdPropiedad(2L);
        reservaDTO.setIdInquilino(3L);
        reservaDTO.setFechaVencimiento(LocalDateTime.now().plusDays(3));

        limpieza = new Limpieza();
        limpieza.setIdLimpieza(1L);
        limpieza.setIdReserva(1L);
        limpieza.setIdPropiedad(2L);
        limpieza.setEstadoLimpieza(EstadoLimpieza.PENDIENTE);
        limpieza.setFechaProgramada(LocalDateTime.now().plusDays(3));
        limpieza.setFechaRealizada(null);
        limpieza.setMotivo(null);
    }

    // PRUEBA 1: agendar limpieza correctamente
    @Test
    void agendarLimpieza_debeRetornarLimpiezaConEstadoPendiente() {
        when(limpiezaRepository.save(any(Limpieza.class))).thenReturn(limpieza);

        LimpiezaResponseDTO resultado = limpiezaService.agendarLimpieza(reservaDTO);

        assertEquals(EstadoLimpieza.PENDIENTE, resultado.getEstadoLimpieza());
        verify(limpiezaRepository, times(1)).save(any(Limpieza.class));
    }

    // PRUEBA 2: obtener limpieza por ID que existe
    @Test
    void obtenerEntidadPorId_debeRetornarLimpieza_cuandoExiste() {
        when(limpiezaRepository.findById(1L)).thenReturn(Optional.of(limpieza));

        Limpieza resultado = limpiezaService.obtenerEntidadPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdLimpieza());
    }

    // PRUEBA 3: obtener limpieza por ID que NO existe — lanza excepción
    @Test
    void obtenerEntidadPorId_debeLanzarExcepcion_cuandoNoExiste() {
        when(limpiezaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LimpiezaNoEncontradaException.class, () ->
                limpiezaService.obtenerEntidadPorId(99L));
    }

    // PRUEBA 4: obtener todas las limpiezas
    @Test
    void obtenerTodas_debeRetornarLista() {
        when(limpiezaRepository.findAll()).thenReturn(List.of(limpieza));

        List<Limpieza> resultado = limpiezaService.obtenerTodas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    // PRUEBA 5: obtener por estado
    @Test
    void obtenerPorEstado_debeRetornarLimpiezasPendientes() {
        when(limpiezaRepository.findByEstadoLimpieza(EstadoLimpieza.PENDIENTE))
                .thenReturn(List.of(limpieza));

        List<Limpieza> resultado = limpiezaService
                .obtenerPorEstado(EstadoLimpieza.PENDIENTE);

        assertEquals(1, resultado.size());
        assertEquals(EstadoLimpieza.PENDIENTE, resultado.get(0).getEstadoLimpieza());
    }

    // PRUEBA 6: cambiar estado — transición válida PENDIENTE → EN_PROCESO
    @Test
    void cambiarEstado_debeActualizar_cuandoTransicionValida() {
        when(limpiezaRepository.findById(1L)).thenReturn(Optional.of(limpieza));
        limpieza.setEstadoLimpieza(EstadoLimpieza.EN_PROCESO);
        when(limpiezaRepository.save(any(Limpieza.class))).thenReturn(limpieza);

        Limpieza resultado = limpiezaService.cambiarEstado(1L, EstadoLimpieza.EN_PROCESO);

        assertEquals(EstadoLimpieza.EN_PROCESO, resultado.getEstadoLimpieza());
        verify(rabbitTemplate, times(1))
                .convertAndSend(anyString(), anyString(), any(Object.class));
    }

    // PRUEBA 7: cambiar estado — transición inválida lanza excepción
    @Test
    void cambiarEstado_debeLanzarExcepcion_cuandoTransicionInvalida() {
        limpieza.setEstadoLimpieza(EstadoLimpieza.COMPLETADA);
        when(limpiezaRepository.findById(1L)).thenReturn(Optional.of(limpieza));

        assertThrows(IllegalArgumentException.class, () ->
                limpiezaService.cambiarEstado(1L, EstadoLimpieza.PENDIENTE));
    }

    // PRUEBA 8: cancelar por sistema correctamente
    @Test
    void cancelarPorSistema_debeActualizarEstado() {
        when(limpiezaRepository.findById(1L)).thenReturn(Optional.of(limpieza));
        limpieza.setEstadoLimpieza(EstadoLimpieza.CANCELADA_POR_SISTEMA);
        when(limpiezaRepository.save(any(Limpieza.class))).thenReturn(limpieza);

        Limpieza resultado = limpiezaService.cancelarPorSistema(1L, "Reserva cancelada");

        assertEquals(EstadoLimpieza.CANCELADA_POR_SISTEMA, resultado.getEstadoLimpieza());
        verify(limpiezaRepository, times(1)).save(any(Limpieza.class));
    }
}
