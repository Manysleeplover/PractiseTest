package ru.aston.romanov.practical.services.operations;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.aston.romanov.practical.constants.OperationEnum;
import ru.aston.romanov.practical.dto.OperationDTO;
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
    public TransactionDTO process(OperationDTO operationDTO) throws InvalidPinCodeException, NoAccountPresentException, InsufficientFundsException {
        Account account = accountRepo.findById(operationDTO.getTransferFromAccountId()).orElseThrow(NoAccountPresentException::new);
        Withdraw transaction = Withdraw.builder()
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

        if(account.getBalance().compareTo(operationDTO.getOperationSum()) < 0){
            String exceptionMessage = "Insufficient funds exception, please make sure there are enough funds in your account";
            transaction.setException(exceptionMessage);
            account.addTransaction(transaction);
            accountRepo.save(account);
            throw new InsufficientFundsException();
        }

        account.setBalance(account.getBalance().subtract(operationDTO.getOperationSum()));
        account.addTransaction(transaction);
        accountRepo.save(account);
        TransactionDTO transactionDTO = entityModelMapper.map(transaction, TransactionDTO.class);
        transactionDTO.setOperation_type(this.type());


        return transactionDTO;
    }

    @Override
    public String type() {
        return OperationEnum.WITHDRAW.getName();
    }
}
