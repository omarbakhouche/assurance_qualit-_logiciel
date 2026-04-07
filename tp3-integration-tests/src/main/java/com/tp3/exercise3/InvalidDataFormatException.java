package com.tp3.exercise3;

/**
 * Exception levée lorsque le format de la réponse de l'API est incompatible.
 */
public class InvalidDataFormatException extends RuntimeException {
    public InvalidDataFormatException(String message) {
        super(message);
    }
}
