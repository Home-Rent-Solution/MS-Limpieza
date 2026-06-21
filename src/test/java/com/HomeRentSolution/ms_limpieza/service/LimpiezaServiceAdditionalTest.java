package com.HomeRentSolution.ms_limpieza.service;

import com.HomeRentSolution.ms_limpieza.client.ReservaClient;
import com.HomeRentSolution.ms_limpieza.config.AppConfig;
import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.exception.LimpiezaNoEncontradaException;
import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.repository.LimpiezaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LimpiezaServiceAdditionalTest {

    @Mock
    private LimpiezaRepository limpiezaRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private ReservaClient reservaClient;

    @InjectMocks
    private LimpiezaService limpiezaService;

    @Test
    void obtenerEntidadPorIdCuandoNoExisteLanzaException() {
        when(limpiezaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LimpiezaNoEncontradaException.class,
                () -> limpiezaService.obtenerEntidadPorId(99L));
    }

    @Test
    void agendarLimpiezaCuandoReservaRemotaNoCoincideLanzaError() {
        ReservaDTO request = crearReserva(10L, 20L);
        ReservaDTO respuestaRemota = crearReserva(99L, 20L);

        when(reservaClient.obtenerPorIdReserva(10L)).thenReturn(respuestaRemota);

        assertThrows(IllegalStateException.class,
                () -> limpiezaService.agendarLimpieza(request));

        verify(limpiezaRepository, never()).save(any());
    }

    @Test
    void cambiarEstadoPendienteACanceladaPorSistemaGuardaYPublicaEvento() {
        Limpieza limpieza = crearLimpieza(1L, EstadoLimpieza.PENDIENTE);

        when(limpiezaRepository.findById(1L)).thenReturn(Optional.of(limpieza));
        when(limpiezaRepository.save(any(Limpieza.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Limpieza resultado = limpiezaService.cambiarEstado(1L, EstadoLimpieza.CANCELADA_POR_SISTEMA);

        assertEquals(EstadoLimpieza.CANCELADA_POR_SISTEMA, resultado.getEstadoLimpieza());
        verify(rabbitTemplate).convertAndSend(
                eq(AppConfig.LIMPIEZAS_EXCHANGE),
                eq(AppConfig.ROUTING_ESTADO_CAMBIADO),
                same(limpieza)
        );
    }

    @Test
    void cancelarPorPersonalCuandoEstaPendienteActualizaMotivoYEstado() {
        Limpieza limpieza = crearLimpieza(2L, EstadoLimpieza.PENDIENTE);

        when(limpiezaRepository.findById(2L)).thenReturn(Optional.of(limpieza));
        when(limpiezaRepository.save(any(Limpieza.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Limpieza resultado = limpiezaService.cancelarPorPersonal(2L, "Personal no disponible");

        assertEquals(EstadoLimpieza.CANCELADA_POR_PERSONAL, resultado.getEstadoLimpieza());
        assertEquals("Personal no disponible", resultado.getMotivo());
    }

    @Test
    void cancelarPorSistemaCuandoNoEstaPendienteLanzaError() {
        Limpieza limpieza = crearLimpieza(3L, EstadoLimpieza.EN_PROCESO);

        when(limpiezaRepository.findById(3L)).thenReturn(Optional.of(limpieza));

        assertThrows(IllegalArgumentException.class,
                () -> limpiezaService.cancelarPorSistema(3L, "Reserva cancelada"));

        verify(limpiezaRepository, never()).save(any());
    }

    private ReservaDTO crearReserva(Long idReserva, Long idPropiedad) {
        ReservaDTO reserva = new ReservaDTO();
        reserva.setIdReserva(idReserva);
        reserva.setIdPropiedad(idPropiedad);
        reserva.setFechaVencimiento(LocalDateTime.of(2026, 6, 30, 11, 0));
        return reserva;
    }

    private Limpieza crearLimpieza(Long id, EstadoLimpieza estado) {
        Limpieza limpieza = new Limpieza();
        limpieza.setIdLimpieza(id);
        limpieza.setIdReserva(10L);
        limpieza.setIdPropiedad(20L);
        limpieza.setFechaProgramada(LocalDateTime.of(2026, 6, 30, 11, 0));
        limpieza.setEstadoLimpieza(estado);
        return limpieza;
    }
}