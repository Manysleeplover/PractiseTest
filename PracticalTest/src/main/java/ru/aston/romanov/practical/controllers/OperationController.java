package ru.aston.romanov.practical.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.dto.operations.OperationRequestDTO;
import ru.aston.romanov.practical.services.OperationsService;
import ru.aston.romanov.practical.utils.validation.OperationMarker;
import ru.aston.romanov.practical.utils.validation.TransferMarker;

import java.util.Optional;

@RestController
@RequestMapping("/operation/")
public class OperationController {

    private final OperationsService operationsService;

    public OperationController(OperationsService operationsService) {
        this.operationsService = operationsService;
    }

    @PostMapping("deposit")
    public ResponseEntity<TransactionDTO> deposit(@Validated(OperationMarker.class) @RequestBody OperationRequestDTO operation){
        TransactionDTO transactionDTO = operationsService.processOperation(operation);
        return ResponseEntity.of(Optional.of(transactionDTO));
    }

    @PostMapping("withdraw")
    public ResponseEntity<TransactionDTO> withdraw(@Validated(OperationMarker.class) @RequestBody OperationRequestDTO operation){
        TransactionDTO transactionDTO = operationsService.processOperation(operation);
        return ResponseEntity.of(Optional.of(transactionDTO));
    }

    @PostMapping("transfer")
    public ResponseEntity<TransactionDTO> transfer(@Validated(TransferMarker.class) @RequestBody OperationRequestDTO operation){
        TransactionDTO transactionDTO = operationsService.processOperation(operation);
        return ResponseEntity.of(Optional.of(transactionDTO));
    }


}
