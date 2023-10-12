package ru.aston.romanov.practical.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.aston.romanov.practical.utils.validation.groups.BeneficiaryInfoMarker;
import ru.aston.romanov.practical.utils.validation.groups.CreateAccountMarker;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeneficiaryDTO {
    private Long id;
    @NotNull(groups = {CreateAccountMarker.class, BeneficiaryInfoMarker.class})
    private String firstname;
    @NotNull(groups = {CreateAccountMarker.class, BeneficiaryInfoMarker.class})
    private String lastname;
    private String patronymic;
    @Size(groups = {CreateAccountMarker.class, BeneficiaryInfoMarker.class}, min = 4, max = 4, message = "The PIN code must be 4 characters long.")
    private String pin;
    private List<AccountDTO> accountList;
}
