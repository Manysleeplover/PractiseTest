package ru.aston.romanov.practical.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.aston.romanov.practical.utils.validation.AccountInfo;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO {
    @NotNull(groups = {AccountInfo.class})
    private Long id;
    private BigDecimal balance;
    @NotNull(groups = {AccountInfo.class})
    @Size(groups = {AccountInfo.class}, min = 4, max = 4)
    private String pin;
    private List<TransactionDTO> transactions;
}
