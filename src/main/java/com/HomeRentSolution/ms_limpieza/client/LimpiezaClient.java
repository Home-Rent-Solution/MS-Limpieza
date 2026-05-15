package com.HomeRentSolution.ms_limpieza.client;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaRequestDTO;
import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ms-limpieza", url= "${ms.limpieza.url}")
public interface LimpiezaClient {

    @GetMapping("/api/limpieza/{id}")
    String obtenerPorId(@PathVariable Long id);

    @PostMapping("/api/limpieza/agendar")
    LimpiezaResponseDTO agendarLimpieza(@RequestBody LimpiezaRequestDTO dto);

    @PutMapping("/api/limpieza/{id}/cancelar")
    void cancelarLimpieza(@PathVariable("id") Long idLimpieza);

}
