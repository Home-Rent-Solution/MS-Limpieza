package com.HomeRentSolution.ms_limpieza.exception;

import com.HomeRentSolution.ms_limpieza.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFoundRetorna404() {
        ResponseEntity<ErrorResponse> response =
                handler.handleNotFound(new LimpiezaNoEncontradaException(99L));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleIllegalArgumentRetorna400() {
        ResponseEntity<ErrorResponse> response =
                handler.handleIllegalArgument(new IllegalArgumentException("Estado invalido"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void handleGeneralRetorna500() {
        ResponseEntity<ErrorResponse> response =
                handler.handleGeneral(new RuntimeException("Error inesperado"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}