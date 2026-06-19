package com.HomeRentSolution.ms_limpieza.exception;

public class LimpiezaNoEncontradaException extends RuntimeException{
    public LimpiezaNoEncontradaException(Long id) {
        super("No se encontró el registro de limpieza con el ID: " + id);
    }
}
