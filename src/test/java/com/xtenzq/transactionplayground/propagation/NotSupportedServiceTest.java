package com.xtenzq.transactionplayground.propagation;

import static com.xtenzq.transactionplayground.propagation.utils.Constants.NOT_SUPPORTED_PROFILE;
import static com.xtenzq.transactionplayground.util.test.MethodNames.getQualifiedMethodName;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xtenzq.transactionplayground.BaseJUnitTest;
import com.xtenzq.transactionplayground.propagation.notsupported.NotSupportedService;
import com.xtenzq.transactionplayground.util.service.TransactionTracker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(NOT_SUPPORTED_PROFILE)
public class NotSupportedServiceTest extends BaseJUnitTest {

    @Autowired
    private NotSupportedService notSupportedService;

    @Test
    void testNotSupportedPropagation() {
        notSupportedService.outer();

        var history = TransactionTracker.getHistory();

        assertEquals(2, history.size(), "There should be two transaction entries");
        assertEquals(
                getQualifiedMethodName(NotSupportedService.class, "outer"),
                history.get(0).getMethodName()
        );
    }
}
