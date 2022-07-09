package com.bobocode.context;

import com.bobocode.context.exceptions.NoSuchBeanException;
import com.bobocode.context.exceptions.NoUniqueBeanException;

import java.util.Map;

/**
 * Custom application context that provides functionality similar to a simple dependency injection container.
 */
public interface ApplicationContext {

    /**
     * Returns a bean by its type.
     *
     * @param beanType type of bean
     * @param <T>      type parameter
     * @return a bean by its type
     * @throws NoSuchBeanException   is expected to be thrown if nothing is found in a context
     * @throws NoUniqueBeanException is expected to be thrown if more than one bean is found in a context
     */
    <T> T getBean(final Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException;

    /**
     * Returns a bean by its name.
     *
     * @param name     bean name
     * @param beanType type of bean
     * @param <T>      type parameter
     * @return a bean by its name
     * @throws NoSuchBeanException is expected to be thrown if nothing is found in a context
     */
    <T> T getBean(final String name, final Class<T> beanType) throws NoSuchBeanException;

    /**
     * Returns a map of beans where the key is its name and the value is the bean
     *
     * @param beanType type of bean
     * @param <T>      type parameter
     * @return a map of beans where the key is it’s name and the value is the bean
     */
    <T> Map<String, T> getAllBeans(final Class<T> beanType);

}
