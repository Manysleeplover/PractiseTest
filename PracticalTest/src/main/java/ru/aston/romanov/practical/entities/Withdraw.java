package ru.aston.romanov.practical.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("withdraw")
public class Withdraw extends Transaction {
    @Builder
    public Withdraw(Long id, BigDecimal balanceBefore, LocalDateTime date, String exception, BigDecimal operationSum, String operationType, Account account) {
        super(id, balanceBefore, date, exception, operationSum, operationType, account);
    }
}
