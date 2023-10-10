package ru.aston.romanov.practical.dto;

import lombok.Data;
import ru.aston.romanov.practical.entities.Account;

import java.util.List;

@Data
public class BeneficiaryDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String patronymic;
    private String pin;
    private List<Account> accountList;
}
