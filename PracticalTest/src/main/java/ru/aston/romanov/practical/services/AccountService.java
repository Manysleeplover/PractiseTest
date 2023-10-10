package ru.aston.romanov.practical.services;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.aston.romanov.practical.dto.AccountDTO;
import ru.aston.romanov.practical.dto.BeneficiaryDTO;
import ru.aston.romanov.practical.entities.Account;
import ru.aston.romanov.practical.entities.Beneficiary;
import ru.aston.romanov.practical.repositories.BeneficiaryRepo;

import java.util.Optional;

import static ru.aston.romanov.practical.constants.BalanceConstants.DEFAULT_BALANCE;

@Service
public class AccountService {
    private final BeneficiaryRepo beneficiaryRepo;
    private final ModelMapper entityModelMapper;
    public AccountService(BeneficiaryRepo beneficiaryRepo, ModelMapper entityModelMapper) {
        this.beneficiaryRepo = beneficiaryRepo;
        this.entityModelMapper = entityModelMapper;
    }

    public AccountDTO createAccount(BeneficiaryDTO beneficiaryDTO) {
        Optional<Beneficiary> optionalBeneficiary = beneficiaryRepo.findBeneficiariesByFirstnameAndLastnameAndPin(
                beneficiaryDTO.getFirstname(),
                beneficiaryDTO.getLastname(),
                beneficiaryDTO.getPin()
        );
        Beneficiary beneficiary;
        Account account = Account.builder().balance(DEFAULT_BALANCE).build();

        beneficiary = optionalBeneficiary.orElseGet(() -> entityModelMapper.map(beneficiaryDTO, Beneficiary.class));
        beneficiary.addAccount(account);
        beneficiaryRepo.save(beneficiary);

        return entityModelMapper.map(account, AccountDTO.class);
    }
}

