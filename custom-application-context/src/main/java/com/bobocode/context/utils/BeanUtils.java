package com.bobocode.context.utils;

import com.bobocode.context.annotations.BoboComponent;

public final class BeanUtils {

    private BeanUtils() {}

    /**
     * Resolves bean's name if the bean is annotated via {@link BoboComponent} annotation.
     * If {@link BoboComponent#name()} parameter has some meaningful value then it will be used as a bean
     * name, otherwise target class name will be taken with leading char put to lower case (DemoService -> demoService).
     *
     * @param targetClass bean class which name should be resolved
     * @return bean name
     */
    public static String resolveBeanName(final Class<?> targetClass) {
        final BoboComponent annotation = targetClass.getAnnotation(BoboComponent.class);
        final String name = annotation.name();
        final String className = targetClass.getSimpleName();
        final String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
        return name.isEmpty() ? beanName : name;
    }
}
