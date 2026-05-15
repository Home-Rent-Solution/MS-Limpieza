package com.HomeRentSolution.ms_limpieza.service;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.repository.LimpiezaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Service
@RequiredArgsConstructor
public class LimpiezaService {

    private final LimpiezaRepository limpiezaRepository;

    public LimpiezaResponseDTO estadoLimpieza()

}

