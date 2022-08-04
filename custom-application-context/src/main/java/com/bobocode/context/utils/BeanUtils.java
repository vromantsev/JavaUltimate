package com.bobocode.context.utils;

import com.bobocode.context.annotations.BoboComponent;

public final class BeanUtils {

    private BeanUtils() {}

    public static String resolveBeanName(final Class<?> targetClass) {
        final BoboComponent annotation = targetClass.getAnnotation(BoboComponent.class);
        final String name = annotation.name();
        final String className = targetClass.getSimpleName();
        final String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
        return name.isEmpty() ? beanName : name;
    }
}
