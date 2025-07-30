package com.xtenzq.transactionplayground.propagation.never;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.NEVER_PROFILE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.xtenzq.transactionplayground.BaseJUnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.IllegalTransactionStateException;

@SpringBootTest
@ActiveProfiles(NEVER_PROFILE)
class NeverServiceTest extends BaseJUnitTest {

    @Autowired
    private NeverService neverService;

    @Test
    public void testTransactionNeverPropagation() {
        assertThrows(IllegalTransactionStateException.class, () -> neverService.outer());
    }

    @Test
    public void testNoTransactionMethod() {
        assertDoesNotThrow(() -> neverService.noTransactionMethod());
    }
}