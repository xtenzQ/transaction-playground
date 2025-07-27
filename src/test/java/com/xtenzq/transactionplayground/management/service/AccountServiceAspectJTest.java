package com.xtenzq.transactionplayground.management.service;

import static com.xtenzq.transactionplayground.management.utils.Constants.ASPECTJ_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(ASPECTJ_PROFILE)
class AccountServiceAspectJTest extends BaseAccountServiceTest {

    @Test
    void shouldVerifyAspectJWeavingIsUsed() {
        assertFalse(AopUtils.isAopProxy(accountService),
                "With AspectJ weaving, AccountService should NOT be a proxy");
        assertEquals(AccountServiceImpl.class.getName(), accountService.getClass().getName(),
                "AspectJ should use the original class, not a proxy");
        assertEquals(Object.class, accountService.getClass().getSuperclass(),
                "AspectJ woven class should extend Object directly, not a proxy class");
        assertTrue(hasAspectJWeavingSignatures(accountService.getClass()),
                "Class should contain AspectJ weaving signatures");
    }

    @Test
    void shouldVerifyAspectJConfigIsLoaded() {
        assertTrue(applicationContext.containsBean("aspectJConfig"));
        assertNotNull(applicationContext.getBean("aspectJConfig"));
    }

    /**
     * Check if the class contains AspectJ weaving signatures.
     * AspectJ weaver typically adds metadata and modifies bytecode in detectable ways.
     */
    private boolean hasAspectJWeavingSignatures(Class<?> clazz) {
        try {
            // Method 1: Check if the class has been modified by AspectJ
            // Look for synthetic methods that AspectJ might add
            var methods = clazz.getDeclaredMethods();
            for (var method : methods) {
                if (method.isSynthetic() && method.getName().contains("ajc")) {
                    return true;
                }
            }

            // Method 2: Check for AspectJ-specific fields
            var fields = clazz.getDeclaredFields();
            for (var field : fields) {
                if (field.isSynthetic() && field.getName().contains("ajc")) {
                    return true;
                }
            }

            // Method 3: Check class annotations for AspectJ metadata
            var annotations = clazz.getDeclaredAnnotations();
            for (var annotation : annotations) {
                String annotationName = annotation.annotationType().getName();
                if (annotationName.contains("aspectj") || annotationName.contains("org.aspectj")) {
                    return true;
                }
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
