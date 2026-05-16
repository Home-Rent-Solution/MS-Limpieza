package com.HomeRentSolution.ms_limpieza.client;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaRequestDTO;
import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-limpieza", url= "${ms.limpieza.url}")
public interface LimpiezaClient {

    @GetMapping("/api/limpieza/{id}")
    String obtenerPorIdLimpieza(@PathVariable("id") Long idLimpieza);

    @PutMapping("/limpieza/{id}/cancelar-por-sistema")
    void cancelarPorSistema(
            @PathVariable("id") Long idLimpieza,
            @RequestBody String motivo  // recibe el String directo, sin DTO
    );

}