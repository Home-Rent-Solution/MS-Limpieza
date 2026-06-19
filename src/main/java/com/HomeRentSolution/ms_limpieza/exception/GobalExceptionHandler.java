package com.HomeRentSolution.ms_limpieza.exception;

import com.HomeRentSolution.ms_limpieza.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GobalExceptionHandler {

    @ExceptionHandler(LimpiezaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(LimpiezaNoEncontradaException ex) {
        log.warn("[EXCEPTION] LimpiezaNoEncontradaException: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Recurso no encontrado", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("[EXCEPTION] Validación de negocio fallida: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Petición inválida / Estado Erróneo", ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("[EXCEPTION] Fallo crítico interno: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Ocurrió un error inesperado en el microservicio de Limpieza.", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
