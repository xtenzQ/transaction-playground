package com.xtenzq.transactionplayground.management.service;

import static com.xtenzq.transactionplayground.management.utils.Constants.MANUAL_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.xtenzq.transactionplayground.BaseJUnitTest;
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
        log.info("Verifying ManualAccountService is not AOP proxied");

        // ManualAccountService should be a direct service without AOP proxying
        // since it manually manages transactions
        String className = manualAccountService.getClass().getName();
        log.info("ManualAccountService class: {}", className);

        assertEquals("com.xtenzq.transactionplayground.service.ManualAccountService", className,
            "ManualAccountService should be the direct class, not a proxy");

        assertFalse(className.contains("$Proxy"),
            "Class name should not contain proxy indicators");
        assertFalse(className.contains("CGLIB"),
            "Class name should not contain CGLIB indicators");

        log.info("Manual service is not proxied as expected");
    }
}
