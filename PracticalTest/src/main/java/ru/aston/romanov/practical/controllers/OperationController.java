package ru.aston.romanov.practical.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.romanov.practical.dto.ErrorDTO;
import ru.aston.romanov.practical.dto.OperationRequestDTO;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.exceptions.InsufficientFundsException;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.services.OperationsService;
import ru.aston.romanov.practical.utils.validation.groups.OperationMarker;
import ru.aston.romanov.practical.utils.validation.groups.TransferMarker;

@RestController
@RequestMapping("/operation/")
public class OperationController {

    private final OperationsService operationsService;

    public OperationController(OperationsService operationsService) {
        this.operationsService = operationsService;
    }


    @Operation(summary = "Endpoint for crediting funds")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation completed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pin supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Account not fount",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PostMapping("deposit")
    public ResponseEntity<TransactionDTO> deposit(@Validated(OperationMarker.class) @RequestBody OperationRequestDTO operation) {
        TransactionDTO transactionDTO;
        try {
            transactionDTO = operationsService.processOperation(operation);
        } catch (InvalidPinCodeException | NoAccountPresentException | InsufficientFundsException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint for withdrawing funds")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation completed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pin supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Account not fount",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Insufficient funds exception",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))}),
    })
    @PostMapping("withdraw")
    public ResponseEntity<TransactionDTO> withdraw(@Validated(OperationMarker.class) @RequestBody OperationRequestDTO operation) {
        TransactionDTO transactionDTO;
        try {
            transactionDTO = operationsService.processOperation(operation);
        } catch (InvalidPinCodeException | NoAccountPresentException | InsufficientFundsException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
    }

    @Operation(summary = "Endpoint for transferring funds from account to account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation completed successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid pin supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Account not fount",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Insufficient funds exception",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))}),
    })
    @PostMapping("transfer")
    public ResponseEntity<TransactionDTO> transfer(@Validated(TransferMarker.class) @RequestBody OperationRequestDTO operation) {
        TransactionDTO transactionDTO;
        try {
            transactionDTO = operationsService.processOperation(operation);
        } catch (InvalidPinCodeException | NoAccountPresentException | InsufficientFundsException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
    }


}
