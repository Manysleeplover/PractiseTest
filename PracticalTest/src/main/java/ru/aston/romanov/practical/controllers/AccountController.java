package ru.aston.romanov.practical.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.romanov.practical.dto.AccountDTO;
import ru.aston.romanov.practical.dto.BeneficiaryDTO;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.exceptions.NoBeneficiaryPresentException;
import ru.aston.romanov.practical.services.AccountService;
import ru.aston.romanov.practical.utils.validation.AccountInfo;
import ru.aston.romanov.practical.utils.validation.BeneficiaryInfoMarker;
import ru.aston.romanov.practical.utils.validation.CreateAccountMarker;

import java.util.Optional;

@RestController
@RequestMapping("/account/")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("create")
    public ResponseEntity<AccountDTO> createBeneficiary(@Validated(CreateAccountMarker.class) @RequestBody BeneficiaryDTO beneficiaryDTO){
        AccountDTO accountDTO = accountService.createAccount(beneficiaryDTO);
        return ResponseEntity.of(Optional.of(accountDTO));
    }

    @PostMapping("beneficiary/info")
    public ResponseEntity<BeneficiaryDTO> getBeneficiaryInfo(@Validated(BeneficiaryInfoMarker.class) @RequestBody BeneficiaryDTO beneficiaryDTO){
        BeneficiaryDTO result;
        try {
             result = accountService.getAccountInfo(beneficiaryDTO);
        } catch (NoBeneficiaryPresentException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ofNullable(result);
    }

    @PostMapping("info")
    public ResponseEntity<AccountDTO> getAccountInfo(@Validated(AccountInfo.class) @RequestBody AccountDTO accountDTO){
        AccountDTO result;
        try {
            result = accountService.getAccountInfo(accountDTO);
        } catch (NoAccountPresentException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ofNullable(result);
    }


}
