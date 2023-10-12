package ru.aston.romanov.practical.exceptions;

public class InvalidPinCodeException extends RuntimeException {
    public InvalidPinCodeException() {
        super("Invalid pin-code, make sure it is correct and try again");
    }
}
