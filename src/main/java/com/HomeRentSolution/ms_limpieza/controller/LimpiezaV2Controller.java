package com.HomeRentSolution.ms_limpieza.controller;

import com.HomeRentSolution.ms_limpieza.assemblers.LimpiezaAssembler;
import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.service.LimpiezaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/limpiezas")
@RequiredArgsConstructor
@Tag(name = "Limpiezas V2 (HATEOAS)",
        description = "Endpoints de limpieza con auto-navegación por enlaces")
public class LimpiezaV2Controller {


    private final LimpiezaService limpiezaService;
    private final LimpiezaAssembler assembler;

    @GetMapping("/{id}")
    public ResponseEntity<LimpiezaResponseDTO> obtenerPorId(@PathVariable Long id) {
        Limpieza entidad = limpiezaService.obtenerEntidadPorId(id);
        return ResponseEntity.ok(assembler.toModel(entidad));
    }

    @GetMapping
    public ResponseEntity<CollectionModel<LimpiezaResponseDTO>> obtenerTodasConEnlaces() {
        List<Limpieza> entidades = limpiezaService.obtenerTodas();
        return ResponseEntity.ok(assembler.toCollectionModel(entidades));
    }

}
