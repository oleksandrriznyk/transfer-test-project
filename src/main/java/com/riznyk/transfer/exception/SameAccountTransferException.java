package com.riznyk.transfer.exception;

public class SameAccountTransferException extends RuntimeException {

    public SameAccountTransferException(Long accountId) {
        super(String.format("Can not transfer funds to the same account. Account id %d", accountId));
    }

}
