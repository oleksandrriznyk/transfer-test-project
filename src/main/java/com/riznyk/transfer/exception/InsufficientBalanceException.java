package com.riznyk.transfer.exception;

import com.riznyk.transfer.entity.Account;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(Account fromAccount, BigDecimal transferAmount) {
        super(String.format("Account with name %s and id %d does not have enough balance to transfer %s currency",
                fromAccount.getName(),
                fromAccount.getId(),
                transferAmount));
    }

}
