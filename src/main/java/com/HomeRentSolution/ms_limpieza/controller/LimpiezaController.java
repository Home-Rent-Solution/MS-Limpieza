package com.HomeRentSolution.ms_limpieza.controller;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.service.LimpiezaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/limpiezas")
@RequiredArgsConstructor
@Tag(name = "Limpiezas V1", description = "API estándar para la gestión operativa del aseo")
public class LimpiezaController {



    private final LimpiezaService limpiezaService;

    @PostMapping
    public ResponseEntity<LimpiezaResponseDTO> crearLimpieza(@RequestBody ReservaDTO reservaDTO) {
        LimpiezaResponseDTO response = limpiezaService.agendarLimpieza(reservaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LimpiezaResponseDTO> obtenerPorId(@PathVariable Long id) {
        Limpieza entidad = limpiezaService.obtenerEntidadPorId(id);
        return ResponseEntity.ok(toResponseDTO(entidad));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<LimpiezaResponseDTO>> obtenerPorEstado(
            @PathVariable EstadoLimpieza estado) {
        List<Limpieza> limpiezas = limpiezaService.obtenerPorEstado(estado);
        List<LimpiezaResponseDTO> dtos = limpiezas.stream().map(this::toResponseDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<LimpiezaResponseDTO> actualizarEstado(
            @PathVariable Long id, @RequestParam EstadoLimpieza nuevoEstado) {
        Limpieza entidadActualizada = limpiezaService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(toResponseDTO(entidadActualizada));
    }

    private LimpiezaResponseDTO toResponseDTO(Limpieza limpieza) {
        LimpiezaResponseDTO dto = new LimpiezaResponseDTO();
        dto.setIdLimpieza(limpieza.getIdLimpieza());
        dto.setIdPropiedad(limpieza.getIdPropiedad());
        dto.setIdReserva(limpieza.getIdReserva());
        dto.setFechaProgramada(limpieza.getFechaProgramada());
        dto.setFechaRealizada(limpieza.getFechaRealizada());
        dto.setEstadoLimpieza(limpieza.getEstadoLimpieza());
        dto.setObservaciones(limpieza.getMotivo());
        return dto;
    }
}
