package com.HomeRentSolution.ms_limpieza.client;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaRequestDTO;
import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface EspecialidadClient {

    @PostMapping("/api/limpieza/agendar")
    LimpiezaResponseDTO agendarLimpieza(@RequestBody LimpiezaRequestDTO dto);

    @PutMapping("/api/limpieza/{id}/cancelar")
    void cancelarLimpieza(@PathVariable("id") Long idLimpieza);

}
