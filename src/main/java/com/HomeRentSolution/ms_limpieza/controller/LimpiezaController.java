package com.HomeRentSolution.ms_limpieza.controller;

import com.HomeRentSolution.ms_limpieza.dto.LimpiezaResponseDTO;
import com.HomeRentSolution.ms_limpieza.dto.ReservaDTO;
import com.HomeRentSolution.ms_limpieza.model.EstadoLimpieza;
import com.HomeRentSolution.ms_limpieza.model.Limpieza;
import com.HomeRentSolution.ms_limpieza.service.LimpiezaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/limpiezas")
@RequiredArgsConstructor
@Tag(name = "Limpiezas V1", description = "API estÃ¡ndar para la gestiÃ³n operativa del aseo")
public class LimpiezaController {



    private final LimpiezaService limpiezaService;

    
    @GetMapping
    @Operation(summary = "Listar todas las limpiezas", description = "Retorna el listado completo de limpiezas registradas.")
    @ApiResponse(responseCode = "200", description = "Listado de limpiezas",
            content = @Content(schema = @Schema(implementation = LimpiezaResponseDTO.class)))
    public ResponseEntity<List<LimpiezaResponseDTO>> obtenerTodas() {
        List<Limpieza> limpiezas = limpiezaService.obtenerTodas();
        List<LimpiezaResponseDTO> dtos = limpiezas.stream().map(this::toResponseDTO).toList();
        return ResponseEntity.ok(dtos);
    }
@PostMapping
    @Operation(summary = "Agendar limpieza", description = "Agenda la limpieza asociada a una reserva y su propiedad.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Limpieza agendada",
                    content = @Content(schema = @Schema(implementation = LimpiezaResponseDTO.class),
                            examples = @ExampleObject(value = "{\"idLimpieza\":1,\"idPropiedad\":10,\"idReserva\":25,\"fechaProgramada\":\"2026-06-30T11:00:00\",\"estadoLimpieza\":\"PENDIENTE\"}"))),
            @ApiResponse(responseCode = "400", description = "Datos de reserva invÃ¡lidos")
    })
    public ResponseEntity<LimpiezaResponseDTO> crearLimpieza(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    description = "Datos de la reserva que origina la limpieza",
                    content = @Content(schema = @Schema(implementation = ReservaDTO.class),
                            examples = @ExampleObject(value = "{\"idReserva\":25,\"idPropiedad\":10,\"fechaFin\":\"2026-06-30T10:00:00\"}")))
            @RequestBody ReservaDTO reservaDTO) {
        LimpiezaResponseDTO response = limpiezaService.agendarLimpieza(reservaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener limpieza por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Limpieza encontrada",
                    content = @Content(schema = @Schema(implementation = LimpiezaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Limpieza no encontrada")
    })
    public ResponseEntity<LimpiezaResponseDTO> obtenerPorId(
            @Parameter(description = "Identificador de la limpieza", example = "1", required = true)
            @PathVariable Long id) {
        Limpieza entidad = limpiezaService.obtenerEntidadPorId(id);
        return ResponseEntity.ok(toResponseDTO(entidad));
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar limpiezas por estado")
    @ApiResponse(responseCode = "200", description = "Listado filtrado por estado")
    public ResponseEntity<List<LimpiezaResponseDTO>> obtenerPorEstado(
            @Parameter(description = "Estado operativo", example = "PENDIENTE", required = true)
            @PathVariable EstadoLimpieza estado) {
        List<Limpieza> limpiezas = limpiezaService.obtenerPorEstado(estado);
        List<LimpiezaResponseDTO> dtos = limpiezas.stream().map(this::toResponseDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de limpieza")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado",
                    content = @Content(schema = @Schema(implementation = LimpiezaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Limpieza no encontrada")
    })
    public ResponseEntity<LimpiezaResponseDTO> actualizarEstado(
            @Parameter(description = "Identificador de la limpieza", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado", example = "COMPLETADA", required = true)
            @RequestParam EstadoLimpieza nuevoEstado) {
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

