package ru.aston.romanov.practical.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {
    private Long id;
    private BigDecimal balanceBefore;
    private LocalDateTime date;
    private BigDecimal operationSum;
    private String operationType;
    private Long transferFromAccountId;
    private Long transferToAccountId;
    private String exception;
}


