package ru.aston.romanov.practical.services;

import org.springframework.stereotype.Service;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.dto.OperationRequestDTO;
import ru.aston.romanov.practical.exceptions.InsufficientFundsException;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.services.operations.AccountOperation;

import java.util.Map;

@Service
public class OperationsService {
    private final Map<String, AccountOperation> operations;

    public OperationsService(Map<String, AccountOperation> operations) {
        this.operations = operations;
    }

    public TransactionDTO processOperation(OperationRequestDTO operationRequestDTO) throws InvalidPinCodeException, NoAccountPresentException, InsufficientFundsException {
        AccountOperation accountOperation = operations.get(operationRequestDTO.getOperationType());
        return accountOperation.process(operationRequestDTO);
    }

}
