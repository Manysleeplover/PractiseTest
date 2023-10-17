package ru.aston.romanov.practical.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.romanov.practical.dto.AccountDTO;
import ru.aston.romanov.practical.dto.BeneficiaryDTO;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.entities.Account;
import ru.aston.romanov.practical.entities.Beneficiary;
import ru.aston.romanov.practical.entities.Transaction;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.exceptions.NoBeneficiaryPresentException;
import ru.aston.romanov.practical.repositories.AccountRepo;
import ru.aston.romanov.practical.repositories.BeneficiaryRepo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ru.aston.romanov.practical.constants.BalanceConstants.DEFAULT_BALANCE;

@Service
public class AccountService {
    private final BeneficiaryRepo beneficiaryRepo;
    private final AccountRepo accountRepo;
    private final ModelMapper entityModelMapper;

    public AccountService(BeneficiaryRepo beneficiaryRepo, AccountRepo accountRepo, ModelMapper entityModelMapper) {
        this.beneficiaryRepo = beneficiaryRepo;
        this.accountRepo = accountRepo;
        this.entityModelMapper = entityModelMapper;
    }

    /**
     * Создаем счёт по фамилии имени и pin-коду бенефициара. Если его нет - создаем нового, если есть - добавляем найденному
     */
    @Transactional
    public AccountDTO createAccount(BeneficiaryDTO beneficiaryDTO) {
        Optional<Beneficiary> optionalBeneficiary = getBeneficiary(beneficiaryDTO);

        Beneficiary beneficiary = optionalBeneficiary.orElseGet(() -> entityModelMapper.map(beneficiaryDTO, Beneficiary.class));
        Account account = Account.builder().balance(DEFAULT_BALANCE).build();
        beneficiary.addAccount(account);
        beneficiaryRepo.save(beneficiary);

        return entityModelMapper.map(account, AccountDTO.class);
    }


    public BeneficiaryDTO getBeneficiaryInfo(BeneficiaryDTO beneficiaryDTO) throws NoBeneficiaryPresentException, InvalidPinCodeException {
        Beneficiary beneficiary = getBeneficiary(beneficiaryDTO).orElseThrow(NoBeneficiaryPresentException::new);

        List<Account> accounts = beneficiary.getAccounts();
        List<AccountDTO> accountDTOs = entityModelMapper.map(accounts, new TypeToken<List<AccountDTO>>() {
        }.getType());
        BeneficiaryDTO resultBeneficiaryDTO = entityModelMapper.map(beneficiary, BeneficiaryDTO.class);
        resultBeneficiaryDTO.setAccountList(accountDTOs);
        resultBeneficiaryDTO.setId(beneficiary.getId());
        return resultBeneficiaryDTO;
    }

    public AccountDTO getAccountInfo(AccountDTO accountDTO) throws NoAccountPresentException, InvalidPinCodeException {
        Account account = accountRepo.findById(accountDTO.getId()).orElseThrow(NoAccountPresentException::new);
        Beneficiary beneficiary = account.getBeneficiary();
        if (!Objects.equals(beneficiary.getPin(), accountDTO.getPin())) {
            throw new InvalidPinCodeException();
        }
        List<? extends Transaction> transactions = account.getTransactions();
        List<TransactionDTO> transactionDTOS = entityModelMapper.map(transactions, new TypeToken<List<TransactionDTO>>() {
        }.getType());

        AccountDTO resultAccountDTO = entityModelMapper.map(account, AccountDTO.class);
        resultAccountDTO.setTransactions(transactionDTOS);
        return resultAccountDTO;
    }

    private Optional<Beneficiary> getBeneficiary(BeneficiaryDTO beneficiaryDTO) {
        return beneficiaryRepo.findBeneficiariesByFirstnameAndLastnameAndPin(
                beneficiaryDTO.getFirstname(),
                beneficiaryDTO.getLastname(),
                beneficiaryDTO.getPin()
        );
    }
}

