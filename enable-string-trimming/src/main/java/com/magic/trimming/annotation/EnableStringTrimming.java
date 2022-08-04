package com.magic.trimming.annotation;

import com.magic.trimming.configuration.TrimmedAnnotationBeanPostProcessorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(TrimmedAnnotationBeanPostProcessorConfig.class)
public @interface EnableStringTrimming {
}
