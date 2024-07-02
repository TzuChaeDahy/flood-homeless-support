package com.tzuchaedahy.exceptions;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super("domain error: " + message);
    }
}
