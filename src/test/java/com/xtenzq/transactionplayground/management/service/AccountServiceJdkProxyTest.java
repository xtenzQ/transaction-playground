package com.xtenzq.transactionplayground.management.service;

import static com.xtenzq.transactionplayground.management.utils.Constants.JDK_PROXY_PROFILE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.xtenzq.transactionplayground.management.config.JdkDynamicProxyConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles(JDK_PROXY_PROFILE)
@SpringBootTest(properties = "spring.aop.proxy-target-class=false")
class AccountServiceJdkProxyTest extends BaseAccountServiceTest {

    @Test
    void verifyJdkDynamicProxyConfigIsLoaded() {
        assertTrue(applicationContext.containsBean("jdkDynamicProxyConfig"),
            "JdkDynamicProxyConfig should be loaded when 'jdk-proxy' profile is active");

        JdkDynamicProxyConfig config = applicationContext.getBean(JdkDynamicProxyConfig.class);
        assertNotNull(config, "JdkDynamicProxyConfig bean should not be null");
    }

    @Test
    void verifyJdkProxyIsUsed() {
        assertTrue(AopUtils.isAopProxy(accountService),
            "AccountService should be an AOP proxy");

        assertTrue(AopUtils.isJdkDynamicProxy(accountService),
            "AccountService should be a JDK Dynamic Proxy, but class was: " +
                    accountService.getClass().getName());
    }

    @Test
    void verifyProxyTargetClass() {
        // Cast to Advised to access proxy configuration
        if (accountService instanceof Advised advised) {
            // For JDK Dynamic Proxy, proxyTargetClass should be false
            assertFalse(advised.isProxyTargetClass(),
                "JDK Dynamic Proxy should have proxyTargetClass = false");

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
