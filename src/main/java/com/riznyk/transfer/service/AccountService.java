package com.riznyk.transfer.service;

import com.riznyk.transfer.entity.Account;
import com.riznyk.transfer.exception.AccountNotFoundException;
import com.riznyk.transfer.exception.TransferFailedException;
import com.riznyk.transfer.repository.AccountRepository;
import com.riznyk.transfer.validator.AccountValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountValidator accountValidator;
    private final AccountRepository accountRepository;

    @Transactional
    @Retryable(retryFor = {OptimisticLockingFailureException.class}, maxAttempts = 2, backoff = @Backoff(delay = 200))
    public void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        log.info("Trying to transfer money. [fromAccountId = {}, toAccountId = {}, amount = {}]", fromAccountId, toAccountId, amount);
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        accountValidator.validate(fromAccount, toAccount, amount);

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
    }

    @Transactional(readOnly = true)
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    @Recover
    public void recover(OptimisticLockingFailureException ex, Account fromAccount, Account toAccount, BigDecimal amount) {
        log.error("Failed to transfer money after multiple attempts due to optimistic locking conflicts. " +
                "FromAccount id = {}, ToAccount id = {}, Amount = {}", fromAccount.getId(), toAccount.getId(), amount, ex);

        throw new TransferFailedException("Failed to transfer money due to system conflicts. Please try again.", ex);
    }

}
