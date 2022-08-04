package com.magic.trimming.configuration;

import com.magic.trimming.annotation.Trimmed;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

public class TrimmedAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        var beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Trimmed.class)) {
            var enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            MethodInterceptor interceptor = (obj, method, args, methodProxy) -> {
                if (args.length > 0) {
                    for (int i = 0; i < args.length; i++) {
                        if (args[i] instanceof String str) {
                            args[i] = str.trim();
                        }
                    }
                }
                final Object result = methodProxy.invokeSuper(obj, args);
                if (result instanceof String s) {
                    return s.trim();
                }
                return result;
            };
            enhancer.setCallback(interceptor);
            return enhancer.create();
        }
        return bean;
    }
}
