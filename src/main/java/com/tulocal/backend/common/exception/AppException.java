package com.tulocal.backend.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Equivalente a AppError de Node.js
 * Excepción base operacional del sistema
 */
public class AppException extends RuntimeException {

    private final HttpStatus status;
    private final boolean isOperational;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.isOperational = true;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public boolean isOperational() {
        return isOperational;
    }

    // ── Métodos estáticos equivalentes a AppError.badRequest(), etc. ──

    public static AppException badRequest(String message) {
        return new AppException(message, HttpStatus.BAD_REQUEST);
    }

    public static AppException unauthorized(String message) {
        return new AppException(message, HttpStatus.UNAUTHORIZED);
    }

    public static AppException unauthorized() {
        return new AppException("No autorizado", HttpStatus.UNAUTHORIZED);
    }

    public static AppException forbidden(String message) {
        return new AppException(message, HttpStatus.FORBIDDEN);
    }

    public static AppException forbidden() {
        return new AppException("Acceso denegado", HttpStatus.FORBIDDEN);
    }

    public static AppException notFound(String message) {
        return new AppException(message, HttpStatus.NOT_FOUND);
    }

    public static AppException notFound() {
        return new AppException("Recurso no encontrado", HttpStatus.NOT_FOUND);
    }

    public static AppException conflict(String message) {
        return new AppException(message, HttpStatus.CONFLICT);
    }

    public static AppException internal(String message) {
        return new AppException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static AppException internal() {
        return new AppException("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}