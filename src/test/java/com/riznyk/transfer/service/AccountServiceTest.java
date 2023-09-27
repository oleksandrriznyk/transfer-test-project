package com.riznyk.transfer.service;

import com.riznyk.transfer.entity.Account;
import com.riznyk.transfer.exception.AccountNotFoundException;
import com.riznyk.transfer.repository.AccountRepository;
import com.riznyk.transfer.validator.AccountValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountValidator accountValidator;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void transferMoneyShouldUpdateBalancesCorrectly() {
        // Given
        Account fromAccount = Account.builder()
                .id(1L)
                .name("Oleksandr")
                .balance(new BigDecimal("100"))
                .build();

        Account toAccount = Account.builder()
                .id(2L)
                .name("Roman")
                .balance(new BigDecimal("100"))
                .build();

        given(accountRepository.findById(1L)).willReturn(Optional.of(fromAccount));
        given(accountRepository.findById(2L)).willReturn(Optional.of(toAccount));

        BigDecimal transferAmount = new BigDecimal("50");

        // When
        accountService.transferMoney(1L, 2L, transferAmount);

        // Then
        assertEquals(new BigDecimal("50"), fromAccount.getBalance());
        assertEquals(new BigDecimal("150"), toAccount.getBalance());
    }

    @Test
    public void transferMoneyShouldThrowWhenFromAccountNotFound() {
        // Given
        given(accountRepository.findById(any())).willReturn(Optional.empty());

        // When & Then
        assertThrows(AccountNotFoundException.class, () -> accountService.transferMoney(1L, 2L, new BigDecimal("50")));
    }

    @Test
    public void getAccountShouldReturnAccountDetails() {
        // Given
        Account account = Account.builder()
                .id(1L)
                .name("Oleksandr")
                .balance(new BigDecimal("100"))
                .build();
        given(accountRepository.findById(1L)).willReturn(Optional.of(account));

        // When
        Account result = accountService.getAccount(1L);

        // Then
        assertEquals(account, result);
    }

    @Test
    public void getAccountShouldThrowWhenAccountNotFound() {
        // Given
        given(accountRepository.findById(any())).willReturn(Optional.empty());

        // When & Then
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(1L));
    }

}