package ru.aston.romanov.practical.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@DiscriminatorValue("deposit")
public class Deposit extends Transaction {
    @Builder
    public Deposit(Long id, BigDecimal balanceBefore, LocalDateTime date, String exception, BigDecimal operationSum, Account account) {
        super(id, balanceBefore, date, exception, operationSum, account);
    }
}
