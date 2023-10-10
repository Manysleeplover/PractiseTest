package ru.aston.romanov.practical.exceptions;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
        super("Insufficient funds exception, please make sure there are enough funds in your account");
    }
}
