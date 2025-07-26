package com.xtenzq.transactionplayground.management.service;

import static com.xtenzq.transactionplayground.management.utils.Constants.DELEGATE_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xtenzq.transactionplayground.base.entity.Account;
import com.xtenzq.transactionplayground.management.service.AccountService;
import com.xtenzq.transactionplayground.management.service.ExternalDelegateService;
import com.xtenzq.transactionplayground.base.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;

@SpringBootTest
@ActiveProfiles(DELEGATE_PROFILE)
public class ExternalDelegateServiceTest {

    @Autowired
    private ExternalDelegateService externalDelegateService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

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
    void testTransactionWorks() {
        externalDelegateService.transfer(fromId, toId, new BigDecimal("25.00"));

        assertEquals(new BigDecimal("75.0000"), accountService.getBalance(fromId));
        assertEquals(new BigDecimal("75.0000"), accountService.getBalance(toId));
    }
}
