package org.safeguard.insurance.exception;

public class DuplicateEntityFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateEntityFoundException(String message) {
        super(message);
    }
}
