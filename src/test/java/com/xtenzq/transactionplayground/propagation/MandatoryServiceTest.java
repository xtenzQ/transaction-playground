package com.xtenzq.transactionplayground.propagation;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.MANDATORY_PROFILE;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.xtenzq.transactionplayground.propagation.mandatory.MandatoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.IllegalTransactionStateException;

@SpringBootTest
@ActiveProfiles(MANDATORY_PROFILE)
public class MandatoryServiceTest {

    @Autowired
    private MandatoryService mandatoryService;

    @Test
    public void testTransactionMandatoryPropagation() {
        assertDoesNotThrow(() -> mandatoryService.outer());
    }

    @Test
    public void testNoTransactionMethod() {
        assertThrows(IllegalTransactionStateException.class, () -> mandatoryService.noTransactionMethod());
    }
}
