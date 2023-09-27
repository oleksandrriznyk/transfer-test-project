package com.riznyk.transfer.validator;

import com.riznyk.transfer.entity.Account;
import com.riznyk.transfer.exception.InsufficientBalanceException;
import com.riznyk.transfer.exception.SameAccountTransferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class AccountValidator {

    public void validate(Account fromAccount, Account toAccount, BigDecimal transferAmount) {
        log.info("Validating possibility to transfer money");
        validateAccounts(fromAccount, toAccount);
        validateBalanceForTransfer(fromAccount, transferAmount);
    }

    private void validateAccounts(Account fromAccount, Account toAccount) {
        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new SameAccountTransferException(fromAccount.getId());
        }
    }

    private void validateBalanceForTransfer(Account fromAccount, BigDecimal transferAmount) {
        if (fromAccount.getBalance().compareTo(transferAmount) < 0) {
            throw new InsufficientBalanceException(fromAccount, transferAmount);
        }
    }

}
