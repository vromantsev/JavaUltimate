package com.bobocode.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DemoApp {

    private static final Logger LOGGER = Logger.getLogger(DemoApp.class.getName());

    public static void main(String[] args) {
        GreetingService helloService = createMethodLoggingProxy(GreetingService.class);
        helloService.hello(); // logs nothing to the console
        helloService.gloryToUkraine(); // logs method invocation to the console
    }

    /**
     * Creates a proxy of the provided class that logs its method invocations. If a method that
     * is marked with {@link LogInvocation} annotation is invoked, it prints to the console the following statement:
     * "[PROXY: Calling method '%s' of the class '%s']%n", where the params are method and class names accordingly.
     *
     * @param targetClass a class that is extended with proxy
     * @param <T>         target class type parameter
     * @return an instance of a proxy class
     */
    public static <T> T createMethodLoggingProxy(Class<T> targetClass) {
        var enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        final MethodInterceptor methodInterceptor = (obj, method, methodArgs, proxy) -> {
            if (method.isAnnotationPresent(LogInvocation.class)) {
                LOGGER.log(Level.INFO, String.format("[PROXY: Calling method '%s' of the class '%s']%n", method.getName(), targetClass.getSimpleName()));
            }
            return proxy.invokeSuper(obj, methodArgs);
        };
        enhancer.setCallback(methodInterceptor);
        return targetClass.cast(enhancer.create());
    }
}
