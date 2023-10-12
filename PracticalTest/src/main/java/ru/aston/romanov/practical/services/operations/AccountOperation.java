package ru.aston.romanov.practical.services.operations;

import ru.aston.romanov.practical.dto.OperationRequestDTO;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.exceptions.InsufficientFundsException;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;

/**
 * Абстракция для всех сущностей операций
 */
public interface AccountOperation {
    /**
     * Процессинг операции
     * @param operationRequestDTO - dto, содержащая инфо об операции
     * @return
     * возвращает dto содержащую инфо о завершенной транзакции
     * @throws InvalidPinCodeException
     * @throws NoAccountPresentException
     * @throws InsufficientFundsException
     */
    TransactionDTO process(OperationRequestDTO operationRequestDTO) throws InvalidPinCodeException, NoAccountPresentException, InsufficientFundsException;

    /**
     * Тип поддерживаемой операции, нужен для разрешения выбора сущности
     */
    String type();
}
