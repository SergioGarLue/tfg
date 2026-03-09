package com.daw.tfg.exception;

/**
 * Excepción personalizada para errores de validación (400).
 */
public class ValidationException extends RuntimeException {
    
    public ValidationException(String message) {
        super(message);
    }
}
