package com.xtenzq.transactionplayground;

import com.xtenzq.transactionplayground.base.repository.AccountRepository;
import com.xtenzq.transactionplayground.base.repository.TransactionLogRepository;
import com.xtenzq.transactionplayground.util.service.TransactionTracker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseJUnitTest {

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected TransactionLogRepository transactionLogRepository;

    @BeforeEach
    public void setup() {
        accountRepository.deleteAll();
        transactionLogRepository.deleteAll();
        TransactionTracker.clearHistory();
    }

    @AfterEach
    public void clearTransactionHistory() {
        accountRepository.deleteAll();
        transactionLogRepository.deleteAll();
        TransactionTracker.clearHistory();
    }
}
