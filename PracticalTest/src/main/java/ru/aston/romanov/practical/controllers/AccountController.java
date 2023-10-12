package ru.aston.romanov.practical.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create an account by firstname lastname and pin, or create Beneficiary and then create account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "account has been created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountDTO.class))}),
    })
    @PostMapping("create")
    public ResponseEntity<AccountDTO> createBeneficiary(@Validated(CreateAccountMarker.class) @RequestBody BeneficiaryDTO beneficiaryDTO) {
        AccountDTO accountDTO = accountService.createAccount(beneficiaryDTO);
        return ResponseEntity.of(Optional.of(accountDTO));
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
        BeneficiaryDTO result;
        try {
            result = accountService.getBeneficiaryInfo(beneficiaryDTO);
        } catch (NoBeneficiaryPresentException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return ResponseEntity.ofNullable(result);
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
    public ResponseEntity<AccountDTO> getAccountInfo(@Validated(AccountInfo.class) @RequestBody AccountDTO accountDTO) {
        AccountDTO result;
        try {
            result = accountService.getAccountInfo(accountDTO);
        } catch (NoAccountPresentException | InvalidPinCodeException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return ResponseEntity.ofNullable(result);
    }
}
