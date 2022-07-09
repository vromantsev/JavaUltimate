package com.bobocode.context.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A field annotated with this annotation is supposed to be injected to a target class.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface Inject {
}
