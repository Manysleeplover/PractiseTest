package ru.aston.romanov.practical.exceptions;

public class NoAccountPresentException extends RuntimeException {
    public NoAccountPresentException() {
        super("Account with this ID not found");
    }

}
