package com.riznyk.transfer;

import com.riznyk.transfer.entity.Account;
import com.riznyk.transfer.repository.AccountRepository;
import com.riznyk.transfer.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"/sql/cleanup.sql", "/sql/insert_account_data.sql"})
public class AccountServiceIntegrationTest {

    private static final Long FROM_ACCOUNT_ID = 1L;
    private static final Long TO_ACCOUNT_ID = 2L;

    @SpyBean
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void transferMoneyConcurrentlyWorksCorrectly() {
        // Given
        Account fromAccount = accountRepository.findById(FROM_ACCOUNT_ID).get();
        Account toAccount = accountRepository.findById(TO_ACCOUNT_ID).get();

        assertThat(fromAccount.getBalance()).isEqualByComparingTo(new BigDecimal("100"));
        assertThat(toAccount.getBalance()).isEqualByComparingTo(new BigDecimal("0"));

        // When
        CompletableFuture<Void> thread1 = CompletableFuture
                .runAsync(() -> accountService.transferMoney(FROM_ACCOUNT_ID, TO_ACCOUNT_ID, new BigDecimal("10")));

        CompletableFuture<Void> thread2 = CompletableFuture
                .runAsync(() -> accountService.transferMoney(FROM_ACCOUNT_ID, TO_ACCOUNT_ID, new BigDecimal("15")));

        CompletableFuture.allOf(thread1, thread2).join();

        // Then
        Account fromAccountAfterTransfer = accountRepository.findById(1L).get();
        Account toAccountAfterTransfer = accountRepository.findById(2L).get();

        assertThat(fromAccountAfterTransfer.getBalance()).isEqualByComparingTo(new BigDecimal("75"));
        assertThat(toAccountAfterTransfer.getBalance()).isEqualByComparingTo(new BigDecimal("25"));
    }

    @Test
    public void transferMoneyShouldUpdateBalancesCorrectly() {
        // Given
        Account fromAccount = accountRepository.findById(FROM_ACCOUNT_ID).get();
        Account toAccount = accountRepository.findById(TO_ACCOUNT_ID).get();

        assertThat(fromAccount.getBalance()).isEqualByComparingTo(new BigDecimal("100"));
        assertThat(toAccount.getBalance()).isEqualByComparingTo(new BigDecimal("0"));

        // When
        accountService.transferMoney(fromAccount.getId(), toAccount.getId(), new BigDecimal("50"));

        Account fromAccountAfterTransfer = accountRepository.findById(1L).get();
        Account toAccountAfterTransfer = accountRepository.findById(2L).get();

        // Then
        assertThat(fromAccountAfterTransfer.getBalance()).isEqualByComparingTo(new BigDecimal("50"));
        assertThat(toAccountAfterTransfer.getBalance()).isEqualByComparingTo(new BigDecimal("50"));
    }

}