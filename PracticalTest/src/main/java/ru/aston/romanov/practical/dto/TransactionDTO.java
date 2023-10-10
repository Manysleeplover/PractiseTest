package ru.aston.romanov.practical.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private BigDecimal balanceBefore;
    private LocalDateTime date;
    private BigDecimal operationSum;
    private String operation_type;
}
