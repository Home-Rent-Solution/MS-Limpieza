package com.HomeRentSolution.ms_limpieza.listeners;

import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.service.LimpiezaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LimpiezaListeners {

    private final LimpiezaService limpiezaService;

    @RabbitListener(queues = "limpiezas.reserva-creada.queue")
    public void recibirNuevaReserva(ReservaDTO creacion) {
        log.info("[RabbitMQ] MS-Limpieza recibió orden de programar limpieza para Reserva ID: {}", creacion.getIdReserva());
        limpiezaService.agendarLimpieza(creacion);
    }

    @RabbitListener(queues = "limpiezas.reserva-cancelada.queue")
    public void recibirReservaCancelada(ReservaDTO evento) {
        log.info("[RabbitMQ] MS-Limpieza recibió evento de cancelación automática de Reserva ID: {}", evento.getIdReserva());
        limpiezaService.cancelarPorSistema(evento.getIdReserva(), "Cancelación automática provocada por el sistema de reservas.");
    }
}
