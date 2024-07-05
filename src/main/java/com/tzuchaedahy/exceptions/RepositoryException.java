package com.tzuchaedahy.exceptions;

public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super("erro de repositorio: " + message);
    }
}
