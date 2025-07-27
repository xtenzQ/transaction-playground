package com.xtenzq.transactionplayground.management.service;

import static com.xtenzq.transactionplayground.management.utils.Constants.CGLIB_PROFILE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.xtenzq.transactionplayground.management.config.CglibConfig;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(CGLIB_PROFILE)
@SpringBootTest(properties = "spring.aop.proxy-target-class=true")
class AccountServiceCglibTest extends BaseAccountServiceTest {

    @Test
    void verifyCglibConfigIsLoaded() {
        assertTrue(applicationContext.containsBean("cglibConfig"),
            "CglibConfig should be loaded when 'cglib' profile is active");
        assertNotNull(applicationContext.getBean(CglibConfig.class),
                "CglibConfig bean should not be null");
    }

    @Test
    void verifyCglibProxyIsUsed() {
        assertTrue(AopUtils.isAopProxy(accountService), "AccountService should be an AOP proxy");
        assertTrue(AopUtils.isCglibProxy(accountService), "AccountService should be a CGLIB proxy");
        assertFalse(AopUtils.isJdkDynamicProxy(accountService),
                "AccountService should NOT be a JDK Dynamic Proxy");
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
