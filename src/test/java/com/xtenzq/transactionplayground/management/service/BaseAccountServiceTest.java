package com.xtenzq.transactionplayground.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.xtenzq.transactionplayground.BaseJUnitTest;
import com.xtenzq.transactionplayground.base.entity.Account;
import com.xtenzq.transactionplayground.base.exception.InsufficientFundsException;
import com.xtenzq.transactionplayground.base.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;

@SpringBootTest
@Slf4j
public abstract class BaseAccountServiceTest extends BaseJUnitTest {

    @Autowired
    protected AccountService accountService;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected ApplicationContext applicationContext;

    protected Long fromId;
    protected Long toId;

    @BeforeEach
    void setUp() {
        log.info("Setting up test data");
        accountRepository.deleteAll();
        var from = accountRepository.save(new Account("Alice", new BigDecimal("100.0000")));
        var to = accountRepository.save(new Account("Bob", new BigDecimal("50.0000")));
        fromId = from.getId();
        toId = to.getId();
        log.info("Test accounts created: fromId={}, toId={}", fromId, toId);
    }

    @Test
    void shouldTransferFundsSuccessfully() {
        log.info("Testing successful fund transfer");

        accountService.transfer(fromId, toId, new BigDecimal("30.00"));

        assertEquals(new BigDecimal("70.0000"), accountService.getBalance(fromId));
        assertEquals(new BigDecimal("80.0000"), accountService.getBalance(toId));

        log.info("Fund transfer completed successfully");
    }

    @Test
    void shouldRollbackWhenInsufficientFunds() {
        log.info("Testing rollback on insufficient funds");

        assertThrows(InsufficientFundsException.class, () ->
                accountService.transfer(fromId, toId, new BigDecimal("200.00"))
        );

        // Verify rollback happened - balances should remain unchanged
        assertEquals(new BigDecimal("100.0000"), accountService.getBalance(fromId));
        assertEquals(new BigDecimal("50.0000"), accountService.getBalance(toId));

        log.info("Rollback on insufficient funds verified");
    }

    @Test
    void shouldGetBalanceForExistingAccount() {
        log.info("Testing balance retrieval for existing account");

        BigDecimal balance = accountService.getBalance(fromId);

        assertEquals(new BigDecimal("100.0000"), balance);

        log.info("Balance retrieval verified: {}", balance);
    }

    @Test
    void shouldThrowExceptionForNonExistentAccount() {
        log.info("Testing exception for non-existent account");

        assertThrows(RuntimeException.class, () ->
                accountService.getBalance(999L)
        );

        log.info("Exception for non-existent account verified");
    }

    @Test
    void shouldHandleZeroAmountTransfer() {
        log.info("Testing zero amount transfer");

        accountService.transfer(fromId, toId, BigDecimal.ZERO);

        // Balances should remain unchanged
        assertEquals(new BigDecimal("100.0000"), accountService.getBalance(fromId));
        assertEquals(new BigDecimal("50.0000"), accountService.getBalance(toId));

        log.info("Zero amount transfer handled correctly");
    }

    @Test
    void shouldHandleSmallAmountTransfer() {
        log.info("Testing small amount transfer");

        accountService.transfer(fromId, toId, new BigDecimal("0.01"));

        assertEquals(new BigDecimal("99.9900"), accountService.getBalance(fromId));
        assertEquals(new BigDecimal("50.0100"), accountService.getBalance(toId));

        log.info("Small amount transfer completed successfully");
    }

    @Test
    void shouldTransferExactBalance() {
        log.info("Testing transfer of exact balance");

        accountService.transfer(fromId, toId, new BigDecimal("100.00"));

        assertEquals(new BigDecimal("0.0000"), accountService.getBalance(fromId));
        assertEquals(new BigDecimal("150.0000"), accountService.getBalance(toId));

        log.info("Exact balance transfer completed successfully");
    }

    @Test
    void shouldRollbackOnAccountNotFound() {
        log.info("Testing rollback when target account not found");

        assertThrows(RuntimeException.class, () ->
                accountService.transfer(fromId, 999L, new BigDecimal("30.00"))
        );

        // Source account balance should remain unchanged
        assertEquals(new BigDecimal("100.0000"), accountService.getBalance(fromId));

        log.info("Rollback on account not found verified");
    }
}