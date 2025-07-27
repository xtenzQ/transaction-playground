package com.xtenzq.transactionplayground.util.reflection;

import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodRef {

    public static String of(Supplier<?> methodRef) {
        return getMethodName(methodRef);
    }

    public static <T, R> String of(Function<T, R> methodRef) {
        return getMethodName(methodRef);
    }

    public static <T> String of(Consumer<T> methodRef) {
        return getMethodName(methodRef);
    }

    public static String of(Runnable methodRef) {
        return getMethodName(methodRef);
    }

    public static String qualifiedName(Class<?> clazz, String methodName) {
        return clazz.getName() + "." + methodName;
    }

    private static String getMethodName(Object methodRef) {
        try {
            // Use lambda metafactory information to extract method name
            Method writeReplace = methodRef.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);

            java.lang.invoke.SerializedLambda serializedLambda =
                (java.lang.invoke.SerializedLambda) writeReplace.invoke(methodRef);

            return serializedLambda.getImplMethodName();
        } catch (Exception e) {
            // Fallback: try to extract from toString
            String toString = methodRef.toString();
            if (toString.contains("::")) {
                String[] parts = toString.split("::");
                if (parts.length > 1) {
                    return parts[1];
                }
            }
            throw new RuntimeException("Could not extract method name from method reference", e);
        }
    }

    public static MethodDescriptor describe(Class<?> clazz, Supplier<?> methodRef) {
        return new MethodDescriptor(clazz, of(methodRef));
    }

    public static <T, R> MethodDescriptor describe(Class<T> clazz, Function<T, R> methodRef) {
        return new MethodDescriptor(clazz, of(methodRef));
    }

    public static <T> MethodDescriptor describe(Class<T> clazz, Consumer<T> methodRef) {
        return new MethodDescriptor(clazz, of(methodRef));
    }

    public static class MethodDescriptor {
        private final Class<?> clazz;
        private final String methodName;

        public MethodDescriptor(Class<?> clazz, String methodName) {
            this.clazz = clazz;
            this.methodName = methodName;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getClassName() {
            return clazz.getSimpleName();
        }

        public String getFullClassName() {
            return clazz.getName();
        }

        public String getQualifiedMethodName() {
            return clazz.getName() + "." + methodName;
        }

        @Override
        public String toString() {
            return getQualifiedMethodName();
        }
    }
}
