package com.xtenzq.transactionplayground.util.service;

import java.util.Stack;

public class TransactionTracker {
    private static ThreadLocal<Stack<MethodContext>> transactionStack = ThreadLocal.withInitial(Stack::new);
    private static final Stack<MethodContext> history = new Stack<>();

    public static void push(MethodContext context) {
        transactionStack.get().push(context);
        synchronized (history) {
            history.push(context);
        }
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
        synchronized (history) {
            return (Stack<MethodContext>) history.clone();
        }
    }

    public static void clearHistory() {
        synchronized (history) {
            history.clear();
        }
    }
}
