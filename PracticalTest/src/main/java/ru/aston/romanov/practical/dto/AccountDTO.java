package ru.aston.romanov.practical.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.aston.romanov.practical.utils.validation.groups.AccountInfoMarker;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO {
    @NotNull(groups = {AccountInfoMarker.class})
    private Long id;
    private BigDecimal balance;
    @NotNull(groups = {AccountInfoMarker.class})
    @Size(groups = {AccountInfoMarker.class}, min = 4, max = 4)
    private String pin;
    private List<TransactionDTO> transactions;
}
