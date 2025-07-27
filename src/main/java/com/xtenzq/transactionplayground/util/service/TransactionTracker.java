package com.xtenzq.transactionplayground.util.service;

import java.util.Stack;

public class TransactionTracker {
    private static ThreadLocal<Stack<MethodContext>> transactionStack = ThreadLocal.withInitial(Stack::new);
    private static ThreadLocal<Stack<MethodContext>> history = ThreadLocal.withInitial(Stack::new);

    public static void push(MethodContext context) {
        transactionStack.get().push(context);
        history.get().push(context);
    }

    public static MethodContext pop() {
        return transactionStack.get().pop();
    }

    public static MethodContext peek() {
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
        for (MethodContext context : transactionStack.get()) {
            tree.append(context.toString()).append("\n");
        }
        return tree.toString();
    }

    public static Stack<MethodContext> getHistory() {
        return history.get();
    }

    public static void clearHistory() {
        history.get().clear();
    }
}
