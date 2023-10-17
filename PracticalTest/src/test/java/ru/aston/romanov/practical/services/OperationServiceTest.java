package ru.aston.romanov.practical.services;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import ru.aston.romanov.practical.constants.OperationEnum;
import ru.aston.romanov.practical.dto.OperationRequestDTO;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.entities.Account;
import ru.aston.romanov.practical.entities.Beneficiary;
import ru.aston.romanov.practical.exceptions.InsufficientFundsException;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.exceptions.UnsupportedAccountOperationException;
import ru.aston.romanov.practical.repositories.AccountRepo;
import ru.aston.romanov.practical.services.operations.AccountOperation;
import ru.aston.romanov.practical.services.operations.DepositOperation;
import ru.aston.romanov.practical.services.operations.TransferOperation;
import ru.aston.romanov.practical.services.operations.WithdrawOperation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OperationServiceTest {

    private final Map<String, AccountOperation> mapMock = new HashMap<>();
    @Mock
    private AccountRepo accountRepo = Mockito.mock(AccountRepo.class);
    private final ModelMapper modelMapper = new ModelMapper();

    private OperationService operationService;

    @BeforeEach
    void initializeOperationMap() {
        Beneficiary beneficiaryIvan = Beneficiary.builder().id(1L).firstname("Ivan").lastname("Ivanov").patronymic("Ivanovich").pin("1234").build();
        Account accountIvan = Account.builder().id(1L).balance(BigDecimal.valueOf(20000)).build();
        beneficiaryIvan.addAccount(accountIvan);

        Beneficiary beneficiaryPetr = Beneficiary.builder().id(2L).firstname("Petrov").lastname("Petr").patronymic("Petrovich").build();
        Account accountPetr = Account.builder().id(2L).balance(BigDecimal.valueOf(20000)).build();
        beneficiaryPetr.addAccount(accountPetr);

        when(accountRepo.findById(1L)).thenReturn(Optional.ofNullable(accountIvan));
        when(accountRepo.findById(2L)).thenReturn(Optional.ofNullable(accountPetr));


        var depositOperation = new DepositOperation(accountRepo, modelMapper);
        var withdrawOperation = new WithdrawOperation(accountRepo, modelMapper);
        var transferOperation = new TransferOperation(accountRepo, modelMapper);

        mapMock.put(OperationEnum.WITHDRAW.getName(), withdrawOperation);
        mapMock.put(OperationEnum.DEPOSIT.getName(), depositOperation);
        mapMock.put(OperationEnum.TRANSFER.getName(), transferOperation);


        operationService = new OperationService(mapMock);

    }

    @Test
    @Tag("Transfer")
    void givenInsufficientFundsForTransfer_thenNoAccountPresentException() {
        OperationRequestDTO operationRequestDTO = OperationRequestDTO.builder()
                .id(1L)
                .operationType(OperationEnum.TRANSFER.getName())
                .operationSum(BigDecimal.valueOf(30000))
                .pin("1234")
                .transferToAccountId(2L)
                .build();
        assertThrows(InsufficientFundsException.class, () -> operationService.processOperation(operationRequestDTO));
    }

    @Test
    @Tag("Transfer")
    void givenInvalidFundsConsumerId_thenNoAccountPresentException() {
        OperationRequestDTO operationRequestDTO = OperationRequestDTO.builder()
                .id(1L)
                .operationType(OperationEnum.TRANSFER.getName())
                .operationSum(BigDecimal.valueOf(10000))
                .pin("1234")
                .transferToAccountId(3L)
                .build();
        assertThrows(NoAccountPresentException.class, () -> operationService.processOperation(operationRequestDTO));
    }

    @Test
    @Tag("Withdraw")
    void givenInsufficientFunds_thenInsufficientFundsException() {
        OperationRequestDTO operationRequestDTO = OperationRequestDTO.builder()
                .id(1L)
                .operationType(OperationEnum.WITHDRAW.getName())
                .operationSum(BigDecimal.valueOf(30000))
                .pin("1234")
                .build();
        assertThrows(InsufficientFundsException.class, () -> operationService.processOperation(operationRequestDTO));
    }

    @ParameterizedTest
    @MethodSource("ru.aston.romanov.practical.services.OperationServiceTest#getValidOperationType")
    @Tag("AllOperations")
    void givenInvalidPinCode_thenThrowInvalidPinCodeException(String operationType) {
        OperationRequestDTO operationRequestDTO = OperationRequestDTO.builder()
                .id(1L)
                .operationType(operationType)
                .operationSum(BigDecimal.valueOf(10000))
                .pin("1235")
                .transferToAccountId(2L)
                .build();
        assertThrows(InvalidPinCodeException.class, () -> operationService.processOperation(operationRequestDTO));
    }

    @ParameterizedTest
    @MethodSource("ru.aston.romanov.practical.services.OperationServiceTest#getValidOperationType")
    @Tag("AllOperations")
    void givenInvalidAccountId_thenThrowNoAccountPresentException(String operationType) {
        OperationRequestDTO operationRequestDTO = OperationRequestDTO.builder()
                .id(3L)
                .operationType(operationType)
                .operationSum(BigDecimal.valueOf(10000))
                .pin("1234")
                .transferToAccountId(2L)
                .build();
        assertThrows(NoAccountPresentException.class, () -> operationService.processOperation(operationRequestDTO));
    }


    @ParameterizedTest
    @MethodSource("ru.aston.romanov.practical.services.OperationServiceTest#getValidOperationType")
    @Tag("AllOperations")
    void givenValidOperationType(String operationType) {
        OperationRequestDTO operationRequestDTO = OperationRequestDTO.builder()
                .id(1L)
                .operationType(operationType)
                .operationSum(BigDecimal.valueOf(10000))
                .pin("1234")
                .transferToAccountId(2L)
                .build();
        TransactionDTO transactionDTO = operationService.processOperation(operationRequestDTO);
        System.out.println(transactionDTO);
    }


    @ParameterizedTest
    @MethodSource("ru.aston.romanov.practical.services.OperationServiceTest#getInvalidOperationType")
    @NullAndEmptySource
    @Tag("AllOperations")
    void giveInvalidOperationType_thenThrowsException(String operationType) {
        OperationRequestDTO operationRequestDTO = OperationRequestDTO.builder()
                .id(1L)
                .operationType(operationType)
                .operationSum(BigDecimal.valueOf(10000))
                .pin("1234")
                .build();
        assertThrows(UnsupportedAccountOperationException.class, () -> operationService.processOperation(operationRequestDTO));
    }

    static Stream<Arguments> getInvalidOperationType() {
        return Stream.of(
                Arguments.of("MOVER"),
                Arguments.of("Transfer"),
                Arguments.of("transfer"),
                Arguments.of("deposit"),
                Arguments.of("DEPOSITE")
        );
    }

    static Stream<Arguments> getValidOperationType() {
        return Stream.of(
                Arguments.of(OperationEnum.WITHDRAW.getName()),
                Arguments.of(OperationEnum.DEPOSIT.getName()),
                Arguments.of(OperationEnum.TRANSFER.getName())
        );
    }
}