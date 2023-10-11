package ru.aston.romanov.practical.exceptions;

public class NoBeneficiaryPresentException extends Exception {
    public NoBeneficiaryPresentException() {
        super("Beneficiary with this FN, LN and PIN not found");
    }

}
