package ru.aston.romanov.practical.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.aston.romanov.practical.dto.AccountDTO;
import ru.aston.romanov.practical.dto.BeneficiaryDTO;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    private final BeneficiaryDTO beneficiaryDTO = BeneficiaryDTO.builder()
            .pin("1234")
            .firstname("Ilya")
            .lastname("Romanov")
            .build();


    @Test
    void createAccount() {
        AccountDTO result = accountService.createAccount(beneficiaryDTO);
        AccountDTO expected = AccountDTO.builder().id(1L).balance(BigDecimal.valueOf(0)).transactions(new ArrayList<>()).build();
        assertEquals(result, expected);
    }

}