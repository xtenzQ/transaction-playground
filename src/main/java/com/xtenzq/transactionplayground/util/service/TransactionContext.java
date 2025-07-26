package com.xtenzq.transactionplayground.util.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.lang.reflect.Method;

@Getter
@RequiredArgsConstructor
public class TransactionContext {
    private final String transactionId;
    private final Method methodName;
    private final Class sourceLocation;
    private final boolean isOuterTransaction;
    private final TransactionContext parentContext;  // Parent context for hierarchical structure

//    @Override
//    public String toString() {
//        // Format with indentation to show parent-child relationship
//        StringBuilder sb = new StringBuilder();
//        if (parentContext != null) {
//            sb.append("    "); // Indentation for child transactions
//            sb.append(parentContext); // Recursively add parent context
//        }
//        sb.append("-> [TransactionId: ").append(transactionId)
//                .append(", Method: ").append(methodName)
//                .append("]");
//        return sb.toString();
//    }
}
