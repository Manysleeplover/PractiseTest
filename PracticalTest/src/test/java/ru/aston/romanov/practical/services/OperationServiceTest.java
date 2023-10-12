package ru.aston.romanov.practical.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.aston.romanov.practical.constants.OperationEnum;
import ru.aston.romanov.practical.dto.OperationRequestDTO;
import ru.aston.romanov.practical.exceptions.UnsupportedAccountOperationException;
import ru.aston.romanov.practical.services.operations.AccountOperation;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OperationServiceTest {

    @Autowired
    private Map<String, AccountOperation> operations;

    @Autowired
    private OperationService operationService;



    @ParameterizedTest
    @EnumSource(OperationEnum.class)
    void processOperation_with_valid_operations_name(OperationEnum operationEnum) {
        String name = operationEnum.getName();
        assertTrue(operations.containsKey(name));
    }

    @ParameterizedTest
    @ValueSource(strings = {"withdraw", "Withrdaw"})
    void processOperation_with_invalid_operations_name(String input){
        OperationRequestDTO operationRequestDTO = OperationRequestDTO.builder().operationType(input).build();
        assertThrows(UnsupportedAccountOperationException.class, () -> operationService.processOperation(operationRequestDTO));
    }

}