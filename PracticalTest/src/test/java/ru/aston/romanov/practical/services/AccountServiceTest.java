package ru.aston.romanov.practical.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import ru.aston.romanov.practical.dto.AccountDTO;
import ru.aston.romanov.practical.dto.BeneficiaryDTO;
import ru.aston.romanov.practical.entities.Account;
import ru.aston.romanov.practical.entities.Beneficiary;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.exceptions.NoBeneficiaryPresentException;
import ru.aston.romanov.practical.repositories.AccountRepo;
import ru.aston.romanov.practical.repositories.BeneficiaryRepo;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @Mock
    private AccountRepo accountRepo = Mockito.mock(AccountRepo.class);
    @Mock
    private BeneficiaryRepo beneficiaryRepo = Mockito.mock(BeneficiaryRepo.class);
    private final ModelMapper modelMapper = new ModelMapper();

    private AccountService accountService;

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
        when(beneficiaryRepo.findBeneficiariesByFirstnameAndLastnameAndPin(beneficiaryIvan.getFirstname(), beneficiaryIvan.getLastname(), beneficiaryIvan.getPin())).thenReturn(Optional.of(beneficiaryIvan));
        when(beneficiaryRepo.findBeneficiariesByFirstnameAndLastnameAndPin(beneficiaryPetr.getFirstname(), beneficiaryPetr.getLastname(), beneficiaryPetr.getPin())).thenReturn(Optional.of(beneficiaryPetr));

        accountService = new AccountService(beneficiaryRepo, accountRepo, modelMapper);
    }


    @Test
    void createAccountTest() {
        BeneficiaryDTO beneficiaryRequest = BeneficiaryDTO.builder().firstname("Ivan").lastname("Ivanov").pin("1234").build();
        AccountDTO accountDTO = AccountDTO.builder().id(null).balance(BigDecimal.valueOf(0)).transactions(Collections.emptyList()).build();
        assertEquals(accountDTO, accountService.createAccount(beneficiaryRequest));
    }


    @Test
    void getBeneficiaryInfo_givenInvalidRequestDTOPin() {
        BeneficiaryDTO beneficiaryRequest = BeneficiaryDTO.builder().id(1L).firstname("Ivan").lastname("Ivanov").patronymic("Ivanovich").pin("1235").build();
        AccountDTO accountDTO = AccountDTO.builder().id(1L).balance(BigDecimal.valueOf(20000)).transactions(Collections.emptyList()).build();
        beneficiaryRequest.setAccountList(List.of(accountDTO));
        assertThrows(NoBeneficiaryPresentException.class, () -> accountService.getBeneficiaryInfo(beneficiaryRequest));
    }

    @Test
    void getBeneficiaryInfo_givenInvalidRequestDTOName() {
        BeneficiaryDTO beneficiaryRequest = BeneficiaryDTO.builder().id(1L).firstname("Ivan").lastname("Petrov").patronymic("Ivanovich").pin("1234").build();
        AccountDTO accountDTO = AccountDTO.builder().id(1L).balance(BigDecimal.valueOf(20000)).transactions(Collections.emptyList()).build();
        beneficiaryRequest.setAccountList(List.of(accountDTO));
        assertThrows(NoBeneficiaryPresentException.class, () -> accountService.getBeneficiaryInfo(beneficiaryRequest));
    }

    @Test
    void getBeneficiaryInfo_givenValidRequestDTO() {
        BeneficiaryDTO beneficiaryRequest = BeneficiaryDTO.builder().id(1L).firstname("Ivan").lastname("Ivanov").patronymic("Ivanovich").pin("1234").build();
        AccountDTO accountDTO = AccountDTO.builder().id(1L).balance(BigDecimal.valueOf(20000)).transactions(Collections.emptyList()).build();
        beneficiaryRequest.setAccountList(List.of(accountDTO));
        assertEquals(beneficiaryRequest, accountService.getBeneficiaryInfo(beneficiaryRequest));
    }

    @Test
    void getAccountInfo_givenValidIdAndPin() {
        AccountDTO accountDTO = AccountDTO.builder().id(1L).pin("1234").build();
        AccountDTO expected = AccountDTO.builder().id(1L).balance(BigDecimal.valueOf(20000)).transactions(Collections.emptyList()).transactions(Collections.emptyList()).build();
        assertEquals(expected, accountService.getAccountInfo(accountDTO));
    }

    @Test
    void getAccountInfo_givenInvalidId() {
        AccountDTO accountDTO = AccountDTO.builder().id(3L).pin("1234").build();
        AccountDTO expected = AccountDTO.builder().id(1L).balance(BigDecimal.valueOf(20000)).transactions(Collections.emptyList()).transactions(Collections.emptyList()).build();
        assertThrows(NoAccountPresentException.class, () -> accountService.getAccountInfo(accountDTO));
    }

    @Test
    void getAccountInfo_givenInvalidPin() {
        AccountDTO accountDTO = AccountDTO.builder().id(1L).pin("1235").build();
        AccountDTO expected = AccountDTO.builder().id(1L).balance(BigDecimal.valueOf(20000)).transactions(Collections.emptyList()).transactions(Collections.emptyList()).build();
        assertThrows(InvalidPinCodeException.class, () -> accountService.getAccountInfo(accountDTO));
    }
}