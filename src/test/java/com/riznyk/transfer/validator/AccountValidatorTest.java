package com.riznyk.transfer.validator;

import com.riznyk.transfer.entity.Account;
import com.riznyk.transfer.exception.InsufficientBalanceException;
import com.riznyk.transfer.exception.SameAccountTransferException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountValidatorTest {

    private final AccountValidator validator = new AccountValidator();

    @Test
    public void testTransferBetweenSameAccountsThrowsException() {
        // Given
        Account fromAccount = Account.builder()
                .id(1L)
                .build();

        // When & Then
        assertThrows(SameAccountTransferException.class, () -> validator.validate(fromAccount, fromAccount, new BigDecimal("10")));
    }

    @Test
    public void testTransferWithInsufficientBalanceThrowsException() {
        // Given
        Account fromAccount = Account.builder()
                .id(1L)
                .balance(new BigDecimal("10"))
                .build();
        Account toAccount = Account.builder()
                .id(2L)
                .balance(new BigDecimal("0"))
                .build();

        // When & Then
        assertThrows(InsufficientBalanceException.class, () -> validator.validate(fromAccount, toAccount, new BigDecimal("50")));
    }

    @Test
    public void testValidTransfer() {
        // Given
        Account fromAccount = Account.builder()
                .id(1L)
                .balance(new BigDecimal("10"))
                .build();
        Account toAccount = Account.builder()
                .id(2L)
                .balance(new BigDecimal("0"))
                .build();

        // When & Then
        assertDoesNotThrow(() -> validator.validate(fromAccount, toAccount, new BigDecimal("5")));
    }

}
