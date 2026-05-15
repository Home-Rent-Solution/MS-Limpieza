package com.HomeRentSolution.ms_limpieza.client;

import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-reservas", url= "${ms.reservas.url}")
public interface ReservaClient {

    @GetMapping("/api/reservas/{id}")
    ReservaDTO obtenerReservaPorId(@PathVariable("id") Long idReserva);
}
