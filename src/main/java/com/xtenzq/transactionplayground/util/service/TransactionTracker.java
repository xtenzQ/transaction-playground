package com.xtenzq.transactionplayground.util.service;

import java.util.Stack;

public class TransactionTracker {
    private static ThreadLocal<Stack<TransactionContext>> transactionStack = ThreadLocal.withInitial(Stack::new);
    private static ThreadLocal<Stack<TransactionContext>> history = ThreadLocal.withInitial(Stack::new);

    public static void push(TransactionContext context) {
        transactionStack.get().push(context);
        history.get().push(context);
    }

    public static TransactionContext pop() {
        return transactionStack.get().pop();
    }

    public static TransactionContext peek() {
        return transactionStack.get().peek();
    }

    public static boolean isTransactionActive() {
        return !transactionStack.get().isEmpty();
    }

    public static void clear() {
        transactionStack.get().clear();
    }

    public static String getTransactionTree() {
        StringBuilder tree = new StringBuilder();
        for (TransactionContext context : transactionStack.get()) {
            tree.append(context.toString()).append("\n");
        }
        return tree.toString();
    }

    public static Stack<TransactionContext> getHistory() {
        return history.get();
    }
}
