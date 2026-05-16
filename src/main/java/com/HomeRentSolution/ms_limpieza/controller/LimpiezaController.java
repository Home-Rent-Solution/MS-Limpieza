package com.HomeRentSolution.ms_limpieza.controller;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.service.LimpiezaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/limpieza")
@RequiredArgsConstructor
public class LimpiezaController {


    private final LimpiezaService limpiezaService;

    @GetMapping("/{id}")
    public ResponseEntity<LimpiezaResponseDTO> obtenerPorId(@PathVariable Long id) {

        LimpiezaResponseDTO busXId = limpiezaService.buscarPorId(id);

        return ResponseEntity.ok(busXId);
    }

    @GetMapping
    public List<LimpiezaResponseDTO> obtenerTodas() {
        return limpiezaService.buscarTodas();
    }

    @GetMapping("/estado/{estado}")
    public List<LimpiezaResponseDTO> buscarPorEstado(@PathVariable("estado") String estado) {
        EstadoLimpieza estadoEnum = EstadoLimpieza.valueOf(estado.toUpperCase());
        List<Limpieza> limpiezas = limpiezaService.buscarPorEstado(estadoEnum);

        // Cambiamos "this" por "limpiezaService"
        return limpiezas.stream()
                .map(limpiezaService::toResponseDTO)
                .toList();
    }

    @PutMapping("/{id}/cancelar-por-sistema")
    public ResponseEntity<?> cancelarPorSistema(
            @PathVariable Long id,
            @RequestBody String observaciones) {  // ← String directo, no DTO
        try {
            return ResponseEntity.ok(limpiezaService.cancelarPorSistema(id, observaciones));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancelar-por-personal")
    public ResponseEntity<?> cancelarPorPersonal(
            @PathVariable Long id,
            @RequestBody ReservaDTO request) {
        try {
            return ResponseEntity.ok(limpiezaService.cancelarPorPersonal(id, request.getMotivo()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 1. Personal inicia limpieza
    @PatchMapping("/{idLimpieza}/iniciar")
    public ResponseEntity<LimpiezaResponseDTO> iniciarLimpieza(@PathVariable Long idLimpieza) {
        LimpiezaResponseDTO response = limpiezaService.cambiarEstado(idLimpieza, EstadoLimpieza.EN_PROCESO);
        return ResponseEntity.ok(response);
    }

    // 2. Personal completa limpieza
    @PatchMapping("/{idLimpieza}/completar")
    public ResponseEntity<LimpiezaResponseDTO> completarLimpieza(@PathVariable Long idLimpieza) {
        LimpiezaResponseDTO response = limpiezaService.cambiarEstado(idLimpieza, EstadoLimpieza.COMPLETADA);
        return ResponseEntity.ok(response);
    }

    // 3. Personal cancela por problema en terreno
    @PostMapping("/{idLimpieza}/cancelar-terreno")
    public ResponseEntity<LimpiezaResponseDTO> cancelarPorTerreno(
            @PathVariable Long idLimpieza,
            @RequestBody String observaciones) {
        LimpiezaResponseDTO response = limpiezaService.cancelarLimpieza(
                idLimpieza,
                EstadoLimpieza.CANCELADA_POR_PERSONAL,
                observaciones
        );
        return ResponseEntity.ok(response);
    }
}
