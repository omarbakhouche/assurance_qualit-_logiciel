package com.tp3.exercise3;

/**
 * Exception levée lors d'un échec d'appel à l'API externe.
 */
public class ApiCallException extends RuntimeException {
    public ApiCallException(String message) {
        super(message);
    }

    public ApiCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
