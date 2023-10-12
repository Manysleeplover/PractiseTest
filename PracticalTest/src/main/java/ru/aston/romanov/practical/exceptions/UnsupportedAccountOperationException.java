package ru.aston.romanov.practical.exceptions;

public class UnsupportedAccountOperationException extends RuntimeException {
    public UnsupportedAccountOperationException(String message) {
        super("Unsupported account operation: " + message);
    }
}
