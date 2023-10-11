package ru.aston.romanov.practical.dto.operations;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.aston.romanov.practical.utils.validation.OperationMarker;
import ru.aston.romanov.practical.utils.validation.TransferMarker;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationRequestDTO {

    @NotNull(groups = OperationMarker.class)
    private Long id;
    @NotNull(groups = OperationMarker.class)
    @Size(groups = OperationMarker.class, min = 4, max = 4, message = "The PIN code must be 4 characters long.")
    private String pin;
    @NotNull(groups = OperationMarker.class)
    @Positive(groups = OperationMarker.class)
    private BigDecimal operationSum;
    @NotNull(groups = OperationMarker.class)
    private String operationType;
    @NotNull(groups = TransferMarker.class)
    private Long transferToAccountId;
}
