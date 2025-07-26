package com.xtenzq.transactionplayground.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.xtenzq.transactionplayground.entity.Account;
import com.xtenzq.transactionplayground.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.math.BigDecimal;

@SpringBootTest
@Slf4j
class ManualAccountServiceTest {

    @Autowired
    private ManualAccountService manualAccountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Long fromId;
    private Long toId;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        var from = accountRepository.save(new Account("Alice", new BigDecimal("100.0000")));
        var to = accountRepository.save(new Account("Bob", new BigDecimal("50.0000")));
        fromId = from.getId();
        toId = to.getId();
    }

    @Test
    void shouldVerifyManualTransactionManagement() {
        log.info("Testing manual transaction management");

        // Verify that no transaction is active before the operation
        assertFalse(TransactionSynchronizationManager.isActualTransactionActive(),
            "No transaction should be active before manual transaction operation");

        // Start a manual transaction to verify transaction state during operation
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            assertTrue(TransactionSynchronizationManager.isActualTransactionActive(),
                "Transaction should be active during manual transaction");

            // Perform the transfer within our test transaction
            manualAccountService.transfer(fromId, toId, new BigDecimal("25.00"));

            // Verify the operation completed successfully
            assertEquals(new BigDecimal("75.0000"), manualAccountService.getBalance(fromId));
            assertEquals(new BigDecimal("75.0000"), manualAccountService.getBalance(toId));

            transactionManager.commit(status);
            log.info("Manual transaction management verified");
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Test
    void shouldVerifyServiceIsNotProxied() {
        log.info("Verifying ManualAccountService is not AOP proxied");

        // ManualAccountService should be a direct service without AOP proxying
        // since it manually manages transactions
        String className = manualAccountService.getClass().getName();
        log.info("ManualAccountService class: {}", className);

        assertEquals("com.xtenzq.transactionplayground.service.ManualAccountService", className,
            "ManualAccountService should be the direct class, not a proxy");

        assertFalse(className.contains("$Proxy"),
            "Class name should not contain proxy indicators");
        assertFalse(className.contains("CGLIB"),
            "Class name should not contain CGLIB indicators");

        log.info("Manual service is not proxied as expected");
    }
}
