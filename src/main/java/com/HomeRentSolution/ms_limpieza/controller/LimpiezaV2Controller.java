package com.HomeRentSolution.ms_limpieza.controller;

import com.HomeRentSolution.ms_limpieza.assemblers.LimpiezaAssembler;
import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.service.LimpiezaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/v2/limpiezas")
@Tag(name = "Limpiezas V2 (HATEOAS)",
        description = "Endpoints de limpieza con auto-navegación por enlaces")
public class LimpiezaV2Controller {


    private final LimpiezaService limpiezaService;
    private final LimpiezaAssembler assembler;


    @Autowired
    public LimpiezaV2Controller(LimpiezaService limpiezaService, LimpiezaAssembler assembler) {
        this.limpiezaService = limpiezaService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener limpieza HATEOAS", description = "Retorna la limpieza con enlaces de navegación relacionados.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Recurso HATEOAS", content = @Content(schema = @Schema(implementation = LimpiezaResponseDTO.class))), @ApiResponse(responseCode = "404", description = "Limpieza no encontrada")})
    public ResponseEntity<LimpiezaResponseDTO> obtenerPorId(
            @Parameter(description = "Identificador de la limpieza", example = "1", required = true) @PathVariable Long id) {
        Limpieza entidad = limpiezaService.obtenerEntidadPorId(id);
        return ResponseEntity.ok(assembler.toModel(entidad));
    }

    @GetMapping
    @Operation(summary = "Listar limpiezas HATEOAS", description = "Retorna la colección de limpiezas con enlaces de navegación.")
    @ApiResponse(responseCode = "200", description = "Colección HATEOAS")
    public ResponseEntity<CollectionModel<LimpiezaResponseDTO>> obtenerTodasConEnlaces() {
        List<Limpieza> entidades = limpiezaService.obtenerTodas();
        return ResponseEntity.ok(assembler.toCollectionModel(entidades));
    }
}
