package ru.aston.romanov.practical.exceptions;

public class UnsupportedAccountOperationException extends Exception{
    public UnsupportedAccountOperationException(String message) {
        super("Unsupported account operation: " + message);
    }
}
