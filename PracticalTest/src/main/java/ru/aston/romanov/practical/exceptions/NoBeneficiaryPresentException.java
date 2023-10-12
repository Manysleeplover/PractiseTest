package ru.aston.romanov.practical.exceptions;

public class NoBeneficiaryPresentException extends Exception {
    public NoBeneficiaryPresentException() {
        super("Beneficiary with this firstname, lastname and pin not found");
    }

}
