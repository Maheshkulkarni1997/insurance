package org.safeguard.insurance.exception;

public class DuplicativeEntityFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicativeEntityFoundException(String message) {
        super(message);
    }
}
