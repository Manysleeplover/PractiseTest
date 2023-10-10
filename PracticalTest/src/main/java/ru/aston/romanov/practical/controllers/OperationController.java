package ru.aston.romanov.practical.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aston.romanov.practical.dto.OperationDTO;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.services.OperationsService;

import java.util.Optional;

@RestController
@RequestMapping("/operation")
public class OperationController {

    private final OperationsService operationsService;

    public OperationController(OperationsService operationsService) {
        this.operationsService = operationsService;
    }

    @PostMapping("/deposit")
    @ResponseBody
    public ResponseEntity<TransactionDTO> deposit(@RequestBody OperationDTO operation){
        TransactionDTO transactionDTO = operationsService.processOperation(operation);
        return ResponseEntity.of(Optional.of(transactionDTO));
    }

    @PostMapping("/withdraw")
    @ResponseBody
    public ResponseEntity<TransactionDTO> withdraw(@RequestBody OperationDTO operation){
        TransactionDTO transactionDTO = operationsService.processOperation(operation);
        return ResponseEntity.of(Optional.of(transactionDTO));
    }

    @PostMapping("/transfer")
    @ResponseBody
    public ResponseEntity<TransactionDTO> transfer(@RequestBody OperationDTO operation){
        TransactionDTO transactionDTO = operationsService.processOperation(operation);
        return ResponseEntity.of(Optional.of(transactionDTO));
    }

}
