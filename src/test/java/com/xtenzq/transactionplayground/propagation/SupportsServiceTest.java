package com.xtenzq.transactionplayground.propagation;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.SUPPORTS_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xtenzq.transactionplayground.BaseJUnitTest;
import com.xtenzq.transactionplayground.propagation.supports.SupportsService;
import com.xtenzq.transactionplayground.util.service.TransactionTracker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(SUPPORTS_PROFILE)
public class SupportsServiceTest extends BaseJUnitTest {

    @Autowired
    private SupportsService supportsService;

    @Test
    void testSupportsPropagationFromTransaction() {
        supportsService.outer();

        var history = TransactionTracker.getHistory();

        assertEquals(3, history.size());
    }

    @Test
    void testSupportsPropagationFromNoTransaction() {
        supportsService.noTransaction();

        var history = TransactionTracker.getHistory();

        assertEquals(0, history.size());
    }
}
