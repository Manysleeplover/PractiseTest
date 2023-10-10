package ru.aston.romanov.practical.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.romanov.practical.dto.AccountDTO;
import ru.aston.romanov.practical.dto.BeneficiaryDTO;
import ru.aston.romanov.practical.services.AccountService;

import java.util.Optional;

@RestController()
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountDTO> createBeneficiary(@RequestBody BeneficiaryDTO beneficiaryDTO){
        AccountDTO accountDTO = accountService.createAccount(beneficiaryDTO);
        return ResponseEntity.of(Optional.of(accountDTO));
    }
}
