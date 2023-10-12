package ru.aston.romanov.practical.services.operations;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.aston.romanov.practical.constants.OperationEnum;
import ru.aston.romanov.practical.dto.OperationRequestDTO;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.entities.Account;
import ru.aston.romanov.practical.entities.Beneficiary;
import ru.aston.romanov.practical.entities.Withdraw;
import ru.aston.romanov.practical.exceptions.InsufficientFundsException;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.repositories.AccountRepo;

import java.time.LocalDateTime;
import java.util.Objects;

@Component("WITHDRAW")
public class WithdrawOperation implements AccountOperation {

    private final AccountRepo accountRepo;
    private final ModelMapper entityModelMapper;

    public WithdrawOperation(AccountRepo accountRepo, ModelMapper entityModelMapper) {
        this.accountRepo = accountRepo;
        this.entityModelMapper = entityModelMapper;
    }
    @Override
    @Transactional
    public TransactionDTO process(OperationRequestDTO operationRequestDTO) throws InvalidPinCodeException, NoAccountPresentException, InsufficientFundsException {
        Account account = accountRepo.findById(operationRequestDTO.getId()).orElseThrow(NoAccountPresentException::new);
        Withdraw transaction = Withdraw.builder()
                .balanceBefore(account.getBalance())
                .date(LocalDateTime.now())
                .account(account)
                .operationSum(operationRequestDTO.getOperationSum())
                .build();

        Beneficiary beneficiary = account.getBeneficiary();
        if (!Objects.equals(beneficiary.getPin(), operationRequestDTO.getPin())) {
            String exceptionMessage = "Invalid pin-code, make sure it is correct and try again";
            transaction.setException(exceptionMessage);
            account.addTransaction(transaction);
            accountRepo.save(account);
            throw new InvalidPinCodeException();
        }

        if(account.getBalance().compareTo(operationRequestDTO.getOperationSum()) < 0){
            String exceptionMessage = "Insufficient funds exception, please make sure there are enough funds in your account";
            transaction.setException(exceptionMessage);
            account.addTransaction(transaction);
            accountRepo.save(account);
            throw new InsufficientFundsException();
        }

        account.setBalance(account.getBalance().subtract(operationRequestDTO.getOperationSum()));
        account.addTransaction(transaction);
        accountRepo.save(account);
        TransactionDTO transactionDTO = entityModelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setOperationType(this.type());


        return transactionDTO;
    }

    @Override
    public String type() {
        return OperationEnum.WITHDRAW.getName();
    }

}
