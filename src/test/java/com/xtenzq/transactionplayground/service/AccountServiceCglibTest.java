package com.xtenzq.transactionplayground.service;

import static com.xtenzq.transactionplayground.utils.Constants.CGLIB_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.xtenzq.transactionplayground.config.CglibConfig;
import com.xtenzq.transactionplayground.entity.Account;
import com.xtenzq.transactionplayground.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;

@ActiveProfiles(CGLIB_PROFILE)
@SpringBootTest(properties = "spring.aop.proxy-target-class=true")
class AccountServiceCglibTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ApplicationContext applicationContext;

    private Long fromId;
    private Long toId;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        var from = accountRepository.save(new Account("Alice", new BigDecimal("100.0000")));
        var to = accountRepository.save(new Account("Bob", new BigDecimal("50.0000")));
        fromId = from.getId();
        toId = to.getId();
    }

    @Test
    void transferSuccess() {
        accountService.transfer(fromId, toId, new BigDecimal("30.00"));
        assertEquals(new BigDecimal("70.0000"), accountService.getBalance(fromId));
        assertEquals(new BigDecimal("80.0000"), accountService.getBalance(toId));
    }

    @Test
    void transferInsufficientFundsRollback() {
        assertThrows(InsufficientFundsException.class, () ->
            accountService.transfer(fromId, toId, new BigDecimal("200.00"))
        );
        assertEquals(new BigDecimal("100.0000"), accountService.getBalance(fromId));
        assertEquals(new BigDecimal("50.0000"), accountService.getBalance(toId));
    }

    @Test
    void verifyCglibConfigIsLoaded() {
        // Verify that CglibConfig bean is present in the application context
        assertTrue(applicationContext.containsBean("cglibConfig"),
            "CglibConfig should be loaded when 'cglib' profile is active");

        // Get the actual config bean
        CglibConfig config = applicationContext.getBean(CglibConfig.class);
        assertNotNull(config, "CglibConfig bean should not be null");
    }

    @Test
    void verifyCglibProxyIsUsed() {
        // Check that the service is indeed a proxy using AopUtils
        assertTrue(AopUtils.isAopProxy(accountService),
            "AccountService should be an AOP proxy");

        // Check that it's specifically a CGLIB proxy (not JDK Dynamic Proxy)
        assertTrue(AopUtils.isCglibProxy(accountService),
            "AccountService should be a CGLIB proxy");

        // Ensure it's NOT a JDK Dynamic Proxy
        assertFalse(AopUtils.isJdkDynamicProxy(accountService),
            "AccountService should NOT be a JDK Dynamic Proxy");

        // Verify the proxy class name pattern (CGLIB proxies have $$EnhancerBySpringCGLIB$$ in the name)
        String className = accountService.getClass().getName();
        assertTrue(className.contains("$$EnhancerBySpringCGLIB$$") || className.contains("$$SpringCGLIB$$"),
            "CGLIB proxy class name should contain '$$EnhancerBySpringCGLIB$$' or '$$SpringCGLIB$$', but was: " + className);
    }

    @Test
    void verifyProxyTargetClass() {
        // Cast to Advised to access proxy configuration
        if (accountService instanceof Advised advised) {
            // For CGLIB proxy, proxyTargetClass should be true
            assertTrue(advised.isProxyTargetClass(),
                "CGLIB proxy should have proxyTargetClass = true");
        }
    }
}
