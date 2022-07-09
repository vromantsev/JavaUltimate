package com.bobocode.context.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Class which wants to become a bean, can be annotated with this annotation.
 * A package with annotated bean candidates (classes, annotated with {@link BoboComponent})
 * will be automatically scanned on application startup.
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface BoboComponent {

    /**
     * Name of a bean can be defined by this property. Component has no name by default.
     *
     * @return bean name
     */
    String name() default "";

}
