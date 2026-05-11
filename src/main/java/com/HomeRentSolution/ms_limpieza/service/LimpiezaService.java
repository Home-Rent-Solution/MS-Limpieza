package com.HomeRentSolution.ms_limpieza.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-especialidades", url= "${ms.especialidades.url}")

public class LimpiezaService {
    @GetMapping("/api/especialidades/{id}")
    String obtenerPorId(@PathVariable Long id );
}

