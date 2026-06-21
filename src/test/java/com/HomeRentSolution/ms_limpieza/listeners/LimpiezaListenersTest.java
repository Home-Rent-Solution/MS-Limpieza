package com.HomeRentSolution.ms_limpieza.listeners;

import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.repository.LimpiezaRepository;
import com.HomeRentSolution.ms_limpieza.service.LimpiezaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LimpiezaListenersTest {

    @Mock
    private LimpiezaService limpiezaService;

    @Mock
    private LimpiezaRepository limpiezaRepository;

    @InjectMocks
    private LimpiezaListeners limpiezaListeners;

    @Test
    void recibirNuevaReservaAgendaLimpieza() {
        ReservaDTO reserva = new ReservaDTO();
        reserva.setIdReserva(10L);

        limpiezaListeners.recibirNuevaReserva(reserva);

        verify(limpiezaService).agendarLimpieza(reserva);
    }

    @Test
    void recibirReservaCanceladaConLimpiezaCancelaPorSistema() {
        ReservaDTO evento = new ReservaDTO();
        evento.setIdReserva(20L);

        Limpieza limpieza = new Limpieza();
        limpieza.setIdLimpieza(5L);
        limpieza.setIdReserva(20L);

        when(limpiezaRepository.findByIdReserva(20L)).thenReturn(Optional.of(limpieza));

        limpiezaListeners.recibirReservaCancelada(evento);

        verify(limpiezaService).cancelarPorSistema(eq(5L), contains("Cancel"));
    }

    @Test
    void recibirReservaCanceladaSinLimpiezaNoCancela() {
        ReservaDTO evento = new ReservaDTO();
        evento.setIdReserva(30L);

        when(limpiezaRepository.findByIdReserva(30L)).thenReturn(Optional.empty());

        limpiezaListeners.recibirReservaCancelada(evento);

        verify(limpiezaService, never()).cancelarPorSistema(anyLong(), anyString());
    }
}