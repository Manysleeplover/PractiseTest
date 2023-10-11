package ru.aston.romanov.practical.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.aston.romanov.practical.utils.validation.AccountInfo;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountDTO {
    @NotNull(groups = {AccountInfo.class})
    private Long id;
    private BigDecimal balance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TransactionDTO> transactions;
}
