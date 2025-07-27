package com.xtenzq.transactionplayground.management.service;

import static com.xtenzq.transactionplayground.management.utils.Constants.MANUAL_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xtenzq.transactionplayground.BaseJUnitTest;
import com.xtenzq.transactionplayground.util.service.TransactionTracker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Slf4j
@ActiveProfiles(MANUAL_PROFILE)
class ManualAccountServiceTest extends BaseJUnitTest {

    @Autowired
    private ManualAccountService manualAccountService;

    @Test
    void shouldVerifyServiceIsNotProxied() {
        manualAccountService.execute();

        assertEquals(1, TransactionTracker.getHistory().size());
        assertEquals("manualTransaction", TransactionTracker.getHistory().peek().transactionId());
    }
}
