package ru.aston.romanov.practical.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {
    private Long id;
    private BigDecimal balance;
}
