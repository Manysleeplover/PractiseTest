package ru.aston.romanov.practical.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.aston.romanov.practical.dto.ErrorDTO;
import ru.aston.romanov.practical.dto.OperationRequestDTO;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.services.OperationService;
import ru.aston.romanov.practical.utils.validation.groups.OperationMarker;
import ru.aston.romanov.practical.utils.validation.groups.TransferMarker;

@RestController
@RequestMapping("/operation/")
@Slf4j
public class OperationRestController {

    private final OperationService operationService;
    public OperationRestController(OperationService operationService) {
        this.operationService = operationService;
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
                            schema = @Schema(implementation = ErrorDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Unsupported account operation exception",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PostMapping("deposit")
    public TransactionDTO deposit(@Validated(OperationMarker.class) @RequestBody OperationRequestDTO operation) {
        log.info("Received a request to top up to an account");
        TransactionDTO transactionDTO = operationService.processOperation(operation);
        log.info("The request was successfully processed");
        return transactionDTO;
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
            @ApiResponse(responseCode = "400", description = "Unsupported account operation exception",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PostMapping("withdraw")
    public TransactionDTO withdraw(@Validated(OperationMarker.class) @RequestBody OperationRequestDTO operation) {
        log.info("Received a request to withdraw funds from an account");
        TransactionDTO transactionDTO = operationService.processOperation(operation);
        log.info("The request was successfully processed");
        return transactionDTO;
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
            @ApiResponse(responseCode = "400", description = "Unsupported account operation exception",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class))})
    })
    @PostMapping("transfer")
    public TransactionDTO transfer(@Validated(TransferMarker.class) @RequestBody OperationRequestDTO operation) {
        log.info("Received a request to transfer funds");
        TransactionDTO transactionDTO = operationService.processOperation(operation);
        log.info("The request was successfully processed");
        return transactionDTO;
    }
}
