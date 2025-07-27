package com.xtenzq.transactionplayground.util.aspect;

import com.xtenzq.transactionplayground.util.service.MethodContext;
import com.xtenzq.transactionplayground.util.service.TransactionTracker;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class TransactionTrackingAspect {

    @Around("@annotation(jakarta.transaction.Transactional) || " +
            "@annotation(org.springframework.transaction.annotation.Transactional) || " +
            "execution(* org.springframework.transaction.PlatformTransactionManager.*(..))")
    public Object trackTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        Method methodSignature = getMethod(joinPoint);
        String transactionId = getTransactionId();
        boolean isOuterTransaction = !TransactionTracker.isTransactionActive();
        MethodContext parentContext = getParentContext();
        Class<?> sourceLocation = joinPoint.getSourceLocation().getWithinType();

        logMethodInvocation(joinPoint);

        MethodContext context = new MethodContext(transactionId, methodSignature, sourceLocation, isOuterTransaction, parentContext);
        TransactionTracker.push(context);

        try {
            logTransactionStart(context);
            return joinPoint.proceed();
        } finally {
            TransactionTracker.pop();
            printTransactionTree();
        }
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        if (joinPoint.getSignature() instanceof MethodSignature methodSignature) {
            return methodSignature.getMethod();
        } else {
            throw new NoSuchMethodException("The signature is not a MethodSignature.");
        }
    }

    private String getTransactionId() {
        String transactionId = TransactionSynchronizationManager.getCurrentTransactionName();
        return (transactionId != null) ? transactionId : "Transaction-" + System.nanoTime();
    }

    private MethodContext getParentContext() {
        return TransactionTracker.isTransactionActive()
                ? TransactionTracker.peek().transactionId().equals(getTransactionId()) ? null : TransactionTracker.peek()
                : null;
    }

    private void logTransactionStart(MethodContext context) {
        log.info("Transaction Started: {}", context);
    }

    private void printTransactionTree() {
        if (TransactionTracker.isTransactionActive() && TransactionTracker.peek().parentTransaction() != null) {
            log.info("Transaction Tree:\n{}", TransactionTracker.getTransactionTree());
        }
    }

    private void logMethodInvocation(ProceedingJoinPoint joinPoint) {
        String targetMethod = joinPoint.getSignature().toShortString();
        String targetClass = joinPoint.getTarget().getClass().getName();
        String sourceClass = joinPoint.getSourceLocation().getWithinType().getName();
        log.info("Invoked {} of class {} from class {}", targetMethod, targetClass, sourceClass);
    }
}
