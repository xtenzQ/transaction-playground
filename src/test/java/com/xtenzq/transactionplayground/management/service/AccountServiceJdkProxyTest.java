package com.xtenzq.transactionplayground.management.service;

import static com.xtenzq.transactionplayground.management.utils.Constants.JDK_PROXY_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.xtenzq.transactionplayground.management.config.JdkDynamicProxyConfig;
import com.xtenzq.transactionplayground.base.entity.Account;
import com.xtenzq.transactionplayground.management.service.AccountService;
import com.xtenzq.transactionplayground.base.exception.InsufficientFundsException;
import com.xtenzq.transactionplayground.base.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;

@Slf4j
@ActiveProfiles(JDK_PROXY_PROFILE)
@SpringBootTest(properties = "spring.aop.proxy-target-class=false")
class AccountServiceJdkProxyTest {

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
    void verifyJdkDynamicProxyConfigIsLoaded() {
        // Verify that JdkDynamicProxyConfig bean is present in the application context
        assertTrue(applicationContext.containsBean("jdkDynamicProxyConfig"),
            "JdkDynamicProxyConfig should be loaded when 'jdk-proxy' profile is active");

        // Get the actual config bean
        JdkDynamicProxyConfig config = applicationContext.getBean(JdkDynamicProxyConfig.class);
        assertNotNull(config, "JdkDynamicProxyConfig bean should not be null");
    }

    @Test
    void verifyJdkProxyIsUsed() {
        // Debug output to understand what's happening
        log.debug("=== JDK Proxy Test Diagnostics ===");
        log.debug("Active profile: jdk-proxy");
        log.debug("AccountService class: {}", accountService.getClass().getName());
        log.debug("AccountService superclass: {}", accountService.getClass().getSuperclass().getName());
        log.debug("Is AOP proxy: {}", AopUtils.isAopProxy(accountService));
        log.debug("Is CGLIB proxy: {}", AopUtils.isCglibProxy(accountService));
        log.debug("Is JDK Dynamic proxy: {}", AopUtils.isJdkDynamicProxy(accountService));

        log.debug("Implemented interfaces:");
        Class<?>[] interfaces = accountService.getClass().getInterfaces();
        for (Class<?> iface : interfaces) {
            log.debug("  - {}", iface.getName());
        }

        // Check if AccountService interface is being implemented
        boolean implementsAccountService = false;
        for (Class<?> iface : interfaces) {
            if (AccountService.class.equals(iface)) {
                implementsAccountService = true;
                break;
            }
        }
        log.debug("Implements AccountService interface: {}", implementsAccountService);

        // Check Spring AOP configuration
        if (accountService instanceof Advised advised) {
            log.debug("Proxy target class setting: {}", advised.isProxyTargetClass());
            Class<?> targetClass = advised.getTargetClass();
            if (targetClass != null) {
                log.debug("Target object class: {}", targetClass.getName());
            } else {
                log.debug("Target object class: null");
            }
        }

        // The actual assertions - these might fail, and that's what we want to debug
        assertTrue(AopUtils.isAopProxy(accountService),
            "AccountService should be an AOP proxy");

        // This is the assertion that's probably failing
        if (!AopUtils.isJdkDynamicProxy(accountService)) {
            log.warn("⚠️  WARNING: Expected JDK Dynamic Proxy but got CGLIB proxy!");
            log.warn("This could be because:");
            log.warn("1. @EnableTransactionManagement(proxyTargetClass = false) is not working");
            log.warn("2. Spring is defaulting to CGLIB for some reason");
            log.warn("3. The interface is not being detected properly");
        }

        assertTrue(AopUtils.isJdkDynamicProxy(accountService),
            "AccountService should be a JDK Dynamic Proxy, but class was: " + accountService.getClass().getName());
    }

    @Test
    void verifyProxyTargetClass() {
        // Cast to Advised to access proxy configuration
        if (accountService instanceof Advised advised) {
            // For JDK Dynamic Proxy, proxyTargetClass should be false
            assertFalse(advised.isProxyTargetClass(),
                "JDK Dynamic Proxy should have proxyTargetClass = false");

            // Verify that the proxy implements the interface
            Class<?>[] interfaces = accountService.getClass().getInterfaces();
            boolean implementsAccountService = false;
            for (Class<?> iface : interfaces) {
                if (AccountService.class.equals(iface)) {
                    implementsAccountService = true;
                    break;
                }
            }
            assertTrue(implementsAccountService,
                "JDK Dynamic Proxy should implement AccountService interface");
        }
    }
}
