package com.xtenzq.transactionplayground.util.test;

public class MethodNames {

    private static String getMethodName(Class<?> clazz, String methodName) {
        try {
            clazz.getDeclaredMethod(methodName);
            return methodName;
        } catch (NoSuchMethodException e) {
            // Try with common parameter types for overloaded methods
            try {
                // Try common parameter combinations
                clazz.getDeclaredMethod(methodName, new Class<?>[0]); // no params
                return methodName;
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException("Method '" + methodName + "' not found in class " + clazz.getName(), e);
            }
        }
    }

    public static String getQualifiedMethodName(Class<?> clazz, String methodName) {
        return clazz.getName() + "." + getMethodName(clazz, methodName);
    }
}
