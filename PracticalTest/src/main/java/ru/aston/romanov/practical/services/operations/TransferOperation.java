package ru.aston.romanov.practical.services.operations;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.aston.romanov.practical.constants.OperationEnum;
import ru.aston.romanov.practical.dto.OperationDTO;
import ru.aston.romanov.practical.dto.TransactionDTO;
import ru.aston.romanov.practical.entities.Account;
import ru.aston.romanov.practical.entities.Beneficiary;
import ru.aston.romanov.practical.entities.Transfer;
import ru.aston.romanov.practical.exceptions.InsufficientFundsException;
import ru.aston.romanov.practical.exceptions.InvalidPinCodeException;
import ru.aston.romanov.practical.exceptions.NoAccountPresentException;
import ru.aston.romanov.practical.repositories.AccountRepo;

import java.time.LocalDateTime;
import java.util.Objects;

@Component("TRANSFER")
public class TransferOperation implements AccountOperation {

    private final AccountRepo accountRepo;
    private final ModelMapper entityModelMapper;

    public TransferOperation(AccountRepo accountRepo, ModelMapper entityModelMapper) {
        this.accountRepo = accountRepo;
        this.entityModelMapper = entityModelMapper;
    }
    @Override
    public TransactionDTO process(OperationDTO operationDTO) throws InvalidPinCodeException, InsufficientFundsException, NoAccountPresentException {
        Account accountFrom = accountRepo.findById(operationDTO.getTransferFromAccountId()).orElseThrow(NoAccountPresentException::new);
        Account accountTo = accountRepo.findById(operationDTO.getTransferToAccountId()).orElseThrow(NoAccountPresentException::new);
        Transfer transactionFrom = Transfer.builder()
                .balanceBefore(accountFrom.getBalance())
                .date(LocalDateTime.now())
                .account(accountFrom)
                .operationSum(operationDTO.getOperationSum())
                .transferToAccountId(operationDTO.getTransferToAccountId())
                .build();

        Beneficiary beneficiary = accountFrom.getBeneficiary();
        if (!Objects.equals(beneficiary.getPin(), operationDTO.getPin())) {
            String exceptionMessage = "Invalid pin-code, make sure it is correct and try again";
            transactionFrom.setException(exceptionMessage);
            accountFrom.addTransaction(transactionFrom);
            accountRepo.save(accountFrom);
            throw new InvalidPinCodeException();
        }

        if(accountFrom.getBalance().compareTo(operationDTO.getOperationSum()) < 0){
            String exceptionMessage = "Insufficient funds exception, please make sure there are enough funds in your accountFrom";
            transactionFrom.setException(exceptionMessage);
            accountFrom.addTransaction(transactionFrom);
            accountRepo.save(accountFrom);
            throw new InsufficientFundsException();
        }
        Transfer transactionTo = Transfer.builder()
                .balanceBefore(accountTo.getBalance())
                .date(LocalDateTime.now())
                .account(accountTo)
                .operationSum(operationDTO.getOperationSum())
                .transferFromAccountId(operationDTO.getTransferFromAccountId())
                .build();

        accountFrom.setBalance(accountFrom.getBalance().subtract(operationDTO.getOperationSum()));
        accountFrom.addTransaction(transactionFrom);

        accountTo.setBalance(accountTo.getBalance().add(operationDTO.getOperationSum()));
        accountTo.addTransaction(transactionTo);

        accountRepo.save(accountFrom);
        accountRepo.save(accountTo);

        TransactionDTO transactionDTO = entityModelMapper.map(transactionFrom, TransactionDTO.class);
        transactionDTO.setOperation_type(this.type());


        return transactionDTO;
    }

    @Override
    public String type() {
        return OperationEnum.TRANSFER.getName();
    }
}
