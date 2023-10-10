package ru.aston.romanov.practical.exceptions;

public class NoAccountPresentException extends Exception{
    public NoAccountPresentException() {
        super("Account with this ID not found");
    }

}
