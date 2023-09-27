package com.riznyk.transfer.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long accountId) {
        super(String.format("Account with id %d is not found", accountId));
    }

}
