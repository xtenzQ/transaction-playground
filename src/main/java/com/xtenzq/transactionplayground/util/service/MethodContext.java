package com.xtenzq.transactionplayground.util.service;

import java.lang.reflect.Method;

public record MethodContext(String transactionId,
                            Method executingMethod,
                            Class<?> sourceLocation,
                            boolean isOuterTransaction,
                            MethodContext parentTransaction) {

    public String getMethodName() {
        return executingMethod.getDeclaringClass().getName() + "." + executingMethod.getName();
    }
}
