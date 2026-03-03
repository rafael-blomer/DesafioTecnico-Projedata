package br.com.rafaelblomer.factory_manager.business.exceptions;

public class DuplicateCodeException extends RuntimeException {
    public DuplicateCodeException(String message) {
        super(message);
    }
}
