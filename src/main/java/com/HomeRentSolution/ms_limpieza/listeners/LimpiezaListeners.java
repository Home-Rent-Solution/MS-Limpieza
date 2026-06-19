package com.HomeRentSolution.ms_limpieza.listeners;

import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.repository.LimpiezaRepository;
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
    private final LimpiezaRepository limpiezaRepository;

    @RabbitListener(queues = "limpiezas.reserva-creada.queue")
    public void recibirNuevaReserva(ReservaDTO creacion) {
        log.info("[RabbitMQ] MS-Limpieza recibió orden de programar limpieza para Reserva ID: {}",
                creacion.getIdReserva());
        limpiezaService.agendarLimpieza(creacion);
    }

    @RabbitListener(queues = "limpiezas.reserva-cancelada.queue")
    public void recibirReservaCancelada(ReservaDTO evento) {
        log.info("[RabbitMQ] MS-Limpieza recibió evento de cancelación para Reserva ID: {}",
                evento.getIdReserva());
        // Buscamos la limpieza asociada a la reserva para obtener su idLimpieza
        limpiezaRepository.findByIdReserva(evento.getIdReserva()).ifPresent(limpieza -> {
            limpiezaService.cancelarPorSistema(
                    limpieza.getIdLimpieza(),
                    "Cancelación automática provocada por el sistema de reservas."
            );
        });
    }

}
