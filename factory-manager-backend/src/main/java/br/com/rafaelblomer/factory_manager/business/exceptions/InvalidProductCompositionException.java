package br.com.rafaelblomer.factory_manager.business.exceptions;

public class InvalidProductCompositionException extends RuntimeException {
    public InvalidProductCompositionException(String message) {
        super(message);
    }
}
