package com.xtenzq.transactionplayground.propagation;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.REQUIRED_PROFILE;
import static com.xtenzq.transactionplayground.util.test.MethodNames.getQualifiedMethodName;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xtenzq.transactionplayground.BaseJUnitTest;
import com.xtenzq.transactionplayground.util.service.TransactionTracker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(REQUIRED_PROFILE)
@SpringBootTest
class RequiredServiceTest extends BaseJUnitTest {

    @Autowired
    private RequiredService requiredService;

    @Test
    void testRequiredPropagation() {
        requiredService.outer();

        var history = TransactionTracker.getHistory();

        assertEquals(
                getQualifiedMethodName(RequiredService.class, "outer"),
                history.get(0).getMethodName()
        );
        assertEquals(
                getQualifiedMethodName(RequiredService.class, "inner"),
                history.get(2).getMethodName()
        );
    }
}