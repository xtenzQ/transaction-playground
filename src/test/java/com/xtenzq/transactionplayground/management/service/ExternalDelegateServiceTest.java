package com.xtenzq.transactionplayground.management.service;

import static com.xtenzq.transactionplayground.management.utils.Constants.DELEGATE_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.xtenzq.transactionplayground.BaseJUnitTest;
import com.xtenzq.transactionplayground.util.service.TransactionTracker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(DELEGATE_PROFILE)
public class ExternalDelegateServiceTest extends BaseJUnitTest {

    @Autowired
    private FirstService firstService;

    @Test
    void shouldBeExecuted() throws NoSuchMethodException {
        firstService.execute();
        assertEquals(3, TransactionTracker.getHistory().size());
        assertEquals(FirstService.class.getDeclaredMethod("execute"), TransactionTracker.getHistory().get(0).executingMethod());
        assertEquals(SecondService.class.getDeclaredMethod("execute"), TransactionTracker.getHistory().get(2).executingMethod());
        assertTrue(TransactionTracker.getHistory().peek().transactionId().contains("FirstService.execute"));
        TransactionTracker.getHistory().forEach(i -> assertTrue(i.transactionId().contains("FirstService.execute")));
    }
}
