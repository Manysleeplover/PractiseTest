package ru.aston.romanov.practical.services.operations;

import ru.aston.romanov.practical.dto.OperationDTO;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.exceptions.InsufficientFundsException;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;

public interface AccountOperation {
    TransactionDTO process(OperationDTO operationDTO) throws InvalidPinCodeException, NoAccountPresentException, InsufficientFundsException;
    String type();
}
