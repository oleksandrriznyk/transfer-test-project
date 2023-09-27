package com.riznyk.transfer.controller;

import com.riznyk.transfer.dto.account.AccountResponseDTO;
import com.riznyk.transfer.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
@Tag(name = "Account controller")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Transfer money between accounts", description = "This operation transfers a specific amount of money from one account to another.")
    public void transferMoney(
            @Parameter(description = "ID of the account to transfer money from", required = true) @RequestParam Long fromAccountId,
            @Parameter(description = "ID of the account to transfer money to", required = true) @RequestParam Long toAccountId,
            @Parameter(description = "Amount of money to transfer", required = true) @RequestParam BigDecimal amount) {

        log.info("Initiating money transfer: fromAccount={}, toAccount={}, amount={}", fromAccountId, toAccountId, amount);
        accountService.transferMoney(fromAccountId, toAccountId, amount);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account details", description = "Fetches details for a specific account given its ID.")
    public ResponseEntity<AccountResponseDTO> getAccount(
            @Parameter(description = "ID of the account to fetch details for", required = true) @PathVariable Long id) {

        log.info("Request to get details for account with id {}", id);
        return ResponseEntity.ok(AccountResponseDTO.fromEntity(accountService.getAccount(id)));
    }

}
