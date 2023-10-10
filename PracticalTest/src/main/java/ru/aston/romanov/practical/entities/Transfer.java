package ru.aston.romanov.practical.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("transfer")
public class Transfer extends Transaction {

    private Long transferFromAccountId;
    private Long transferToAccountId;

    @Builder
    public Transfer(Long id, BigDecimal balanceBefore, LocalDateTime date, String exception, BigDecimal operationSum, Account account, Long transferFromAccountId, Long transferToAccountId) {
        super(id, balanceBefore, date, exception, operationSum, account);
        this.transferFromAccountId = transferFromAccountId;
        this.transferToAccountId = transferToAccountId;
    }
}
