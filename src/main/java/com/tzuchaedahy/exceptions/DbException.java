package com.tzuchaedahy.exceptions;

public class DbException extends RuntimeException {
    public DbException(String message) {
        super("erro no banco de dados: " + message);
    }
}
