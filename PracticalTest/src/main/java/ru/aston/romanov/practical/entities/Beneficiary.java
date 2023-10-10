package ru.aston.romanov.practical.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@ToString(exclude = "accounts")
@EqualsAndHashCode(exclude = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Beneficiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;

    private String lastname;

    private String patronymic;

    private String pin;

    @Builder.Default
    @OneToMany(mappedBy = "beneficiary",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    public void addAccount(Account account){
        accounts.add(account);
        account.setBeneficiary(this);
    }
}


