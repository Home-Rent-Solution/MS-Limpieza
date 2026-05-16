package com.HomeRentSolution.ms_limpieza.controller;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
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

        return ResponseEntity.ok(busXId );
    }

    @GetMapping
    public List<LimpiezaResponseDTO> obtenerTodas() {
        return limpiezaService.buscarTodas();
    }

    @GetMapping("/estado/{estado}")
    public List<LimpiezaResponseDTO> buscarPorEstado(@PathVariable EstadoLimpieza estado){

        return limpiezaService.cambiarEstado(EstadoLimpieza.valueOf(estado));


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
    //             Posibles métodos mínimos para Limpieza
//Controller (endpoints que Reservas va a llamar):
//
//POST /api/limpiezas → crear una limpieza programada (Reservas manda fecha y propiedad)
//
//DELETE /api/limpiezas/{id} → cancelar limpieza programada
//
//GET /api/limpiezas/propiedad/{propiedadId}/estado → saber si la propiedad está limpia o no
//
//PATCH /api/limpiezas/{id}/estado → actualizar estado (cuando el equipo de limpieza reporta)
//
}
