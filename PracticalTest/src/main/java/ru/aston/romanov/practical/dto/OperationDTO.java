package ru.aston.romanov.practical.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OperationDTO {
    private Long transferFromAccountId;
    private String pin;
    private Long transferToAccountId;
    private BigDecimal operationSum;
    private String operationType;
}
