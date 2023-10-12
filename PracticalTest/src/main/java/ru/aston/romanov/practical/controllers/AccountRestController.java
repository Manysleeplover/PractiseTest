package ru.aston.romanov.practical.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.romanov.practical.dto.AccountDTO;
import ru.aston.romanov.practical.dto.BeneficiaryDTO;
import ru.aston.romanov.practical.dto.ErrorDTO;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.exceptions.NoBeneficiaryPresentException;
import ru.aston.romanov.practical.services.AccountService;
import ru.aston.romanov.practical.utils.validation.groups.AccountInfoMarker;
import ru.aston.romanov.practical.utils.validation.groups.BeneficiaryInfoMarker;
import ru.aston.romanov.practical.utils.validation.groups.CreateAccountMarker;

@RestController
@RequestMapping("/account/")
@Slf4j
public class AccountRestController {

    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create an account by firstname lastname and pin, or create Beneficiary and then create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "account has been created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDTO.class))}),
    })
    @PostMapping("create")
    public ResponseEntity<AccountDTO> createBeneficiary(@Validated(CreateAccountMarker.class) @RequestBody BeneficiaryDTO beneficiaryDTO) {
        log.info("Received a request to create an account");
        AccountDTO accountDTO = accountService.createAccount(beneficiaryDTO);
        log.info("Account created successfully");
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }

    @Operation(summary = "Obtaining information about the beneficiary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Beneficiary information received",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BeneficiaryDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pin supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Beneficiary not fount",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PostMapping("beneficiary/info")
    public ResponseEntity<BeneficiaryDTO> getBeneficiaryInfo(@Validated(BeneficiaryInfoMarker.class) @RequestBody BeneficiaryDTO beneficiaryDTO) {
        log.info("Received a request to get Beneficiary info");
        BeneficiaryDTO result;
        try {
            result = accountService.getBeneficiaryInfo(beneficiaryDTO);
        } catch (NoBeneficiaryPresentException | InvalidPinCodeException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        log.info("Beneficiary information successfully received");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(summary = "Obtaining information about the account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account information received",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BeneficiaryDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pin supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Account not fount",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PostMapping("info")
    public ResponseEntity<AccountDTO> getAccountInfo(@Validated(AccountInfoMarker.class) @RequestBody AccountDTO accountDTO) {
        log.info("Received a request to get Account info");
        AccountDTO result;
        try {
            result = accountService.getAccountInfo(accountDTO);
        } catch (NoAccountPresentException | InvalidPinCodeException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        log.info("Account information successfully received");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
