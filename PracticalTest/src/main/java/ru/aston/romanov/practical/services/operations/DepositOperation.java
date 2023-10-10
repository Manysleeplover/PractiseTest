package ru.aston.romanov.practical.services.operations;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.romanov.practical.constants.OperationEnum;
import ru.aston.romanov.practical.dto.OperationDTO;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.entities.Account;
import ru.aston.romanov.practical.entities.Beneficiary;
import ru.aston.romanov.practical.entities.Deposit;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.repositories.AccountRepo;

import java.time.LocalDateTime;
import java.util.Objects;

@Component("DEPOSIT")
public class DepositOperation implements AccountOperation {

    private final AccountRepo accountRepo;
    private final ModelMapper entityModelMapper;

    public DepositOperation(AccountRepo accountRepo, ModelMapper entityModelMapper) {
        this.accountRepo = accountRepo;
        this.entityModelMapper = entityModelMapper;
    }

    @Override
    @Transactional
    public TransactionDTO process(OperationDTO operationDTO) throws InvalidPinCodeException, NoAccountPresentException {
        Account account = accountRepo.findById(operationDTO.getTransferFromAccountId()).orElseThrow(NoAccountPresentException::new);
        Deposit transaction = Deposit.builder()
                .balanceBefore(account.getBalance())
                .date(LocalDateTime.now())
                .account(account)
                .operationSum(operationDTO.getOperationSum())
                .build();

        Beneficiary beneficiary = account.getBeneficiary();
        if (!Objects.equals(beneficiary.getPin(), operationDTO.getPin())) {
            String exceptionMessage = "Invalid pin-code, make sure it is correct and try again";
            transaction.setException(exceptionMessage);
            account.addTransaction(transaction);
            accountRepo.save(account);
            throw new InvalidPinCodeException();
        }

        account.setBalance(account.getBalance().add(operationDTO.getOperationSum()));
        account.addTransaction(transaction);
        accountRepo.save(account);
        TransactionDTO transactionDTO = entityModelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setOperation_type(this.type());

        return transactionDTO;
    }

    @Override
    public String type() {
        return OperationEnum.DEPOSIT.getName();
    }
}
