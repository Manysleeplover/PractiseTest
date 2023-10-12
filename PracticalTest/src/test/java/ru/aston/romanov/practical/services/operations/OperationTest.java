package ru.aston.romanov.practical.services.operations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.aston.romanov.practical.constants.OperationEnum;
import ru.aston.romanov.practical.dto.BeneficiaryDTO;
import ru.aston.romanov.practical.dto.OperationRequestDTO;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.exceptions.InsufficientFundsException;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.services.AccountService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OperationTest {

    @Autowired
    private DepositOperation depositOperation;

    @Autowired
    private AccountService accountService;

    @Autowired
    WithdrawOperation withdrawOperation;

    private final BeneficiaryDTO beneficiaryDTO = BeneficiaryDTO.builder()
            .pin("1234")
            .firstname("Ilya")
            .lastname("Romanov")
            .build();

    @Test
    void deposit() throws InvalidPinCodeException, NoAccountPresentException {
        OperationRequestDTO operationRequestDTO = OperationRequestDTO.builder().operationType(OperationEnum.DEPOSIT.getName()).operationSum(BigDecimal.valueOf(20000)).id(1L).pin("1234").build();
        accountService.createAccount(beneficiaryDTO);

        TransactionDTO process = depositOperation.process(operationRequestDTO);
        assertEquals(process.getId(), 2);
    }

    @Test
    void withdraw_with_no_money_on_account() {
        OperationRequestDTO withdraw = OperationRequestDTO.builder().operationType(OperationEnum.WITHDRAW.getName()).operationSum(BigDecimal.valueOf(20000)).id(1L).pin("1234").build();
        accountService.createAccount(beneficiaryDTO);
        assertThrows(InsufficientFundsException.class, () -> withdrawOperation.process(withdraw));
    }
}