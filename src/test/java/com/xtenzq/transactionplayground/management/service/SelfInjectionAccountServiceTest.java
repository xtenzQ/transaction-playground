package com.xtenzq.transactionplayground.management.service;

import static com.xtenzq.transactionplayground.management.utils.Constants.SELF_PROFILE;
import static com.xtenzq.transactionplayground.util.test.MethodNames.getQualifiedMethodName;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.xtenzq.transactionplayground.BaseJUnitTest;
import com.xtenzq.transactionplayground.util.service.TransactionTracker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(SELF_PROFILE)
public class SelfInjectionAccountServiceTest extends BaseJUnitTest {

    @Autowired
    protected SelfInjectionAccountService selfInjectionAccountService;

    @Test
    void testTransactionInAPrivateMethod() {
        selfInjectionAccountService.outer();

        var latestMethod = TransactionTracker.getHistory();

        assertEquals(
                getQualifiedMethodName(SelfInjectionAccountService.class, "outer"),
                latestMethod.get(0).executingMethod().getDeclaringClass().getName() + "." + latestMethod.get(0).executingMethod().getName()
        );
        assertEquals(
                getQualifiedMethodName(SelfInjectionAccountService.class, "inner"),
                latestMethod.get(2).executingMethod().getDeclaringClass().getName() + "." + latestMethod.get(2).executingMethod().getName()
        );
    }
}
