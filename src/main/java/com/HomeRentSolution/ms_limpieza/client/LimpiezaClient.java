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


}