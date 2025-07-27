package com.xtenzq.transactionplayground;

import com.xtenzq.transactionplayground.util.service.TransactionTracker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseJUnitTest {

    @BeforeEach
    void setup() {
        TransactionTracker.clearHistory();
    }

    @AfterEach
    void clearTransactionHistory() {
        TransactionTracker.clearHistory();
    }
}
