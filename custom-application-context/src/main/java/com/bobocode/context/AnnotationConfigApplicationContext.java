package com.bobocode.context;

import com.bobocode.context.annotations.BoboComponent;
import com.bobocode.context.annotations.Inject;
import com.bobocode.context.exceptions.BeanNameIsNotUnique;
import com.bobocode.context.exceptions.NoSuchBeanException;
import com.bobocode.context.exceptions.NoUniqueBeanException;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

import static com.bobocode.context.utils.BeanUtils.resolveBeanName;
import static java.util.stream.Collectors.toMap;

/**
 * Implementation of {@link ApplicationContext}.
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final ConcurrentMap<String, Object> container = new ConcurrentHashMap<>();

    public AnnotationConfigApplicationContext(final String packageName) {
        Objects.requireNonNull(packageName, "Package name should be not null!");
        initializeBeans(packageName);
        injectBeans();
    }

    /**
     * {@inheritDoc}
     *
     * @param beanType type of bean
     * @param <T>      type parameter
     * @return a bean by its type
     * @throws NoSuchBeanException   is expected to be thrown if nothing is found in a context
     * @throws NoUniqueBeanException is expected to be thrown if more than one bean is found in a context
     */
    @Override
    public <T> T getBean(final Class<T> beanType) throws NoSuchBeanException, NoUniqueBeanException {
        final Map<String, Object> result = this.container.values().stream()
                .filter(bean -> bean.getClass().isAssignableFrom(beanType))
                .collect(toMap(bean -> bean.getClass().getSimpleName(), Function.identity()));
        if (result.isEmpty()) {
            throw new NoSuchBeanException(String.format("Bean of type %s not found!", beanType.getName()));
        }
        if (result.size() > 1) {
            throw new NoUniqueBeanException(String.format("Bean of type %s in not unique!", beanType.getName()));
        }
        return beanType.cast(result.get(beanType.getSimpleName()));
    }

    /**
     * {@inheritDoc}
     *
     * @param name     bean name
     * @param beanType type of bean
     * @param <T>      type parameter
     * @return a bean by its name
     * @throws NoSuchBeanException is expected to be thrown if nothing is found in a context
     */
    @Override
    public <T> T getBean(final String name, final Class<T> beanType) throws NoSuchBeanException {
        return this.container.entrySet().stream()
                .filter(e -> e.getKey().equals(name))
                .map(e -> beanType.cast(e.getValue()))
                .findFirst()
                .orElseThrow(() -> new NoSuchBeanException(String.format("Bean with name %s not found!", name)));
    }

    /**
     * {@inheritDoc}
     *
     * @param beanType type of bean
     * @param <T>      type parameter
     * @return a map of beans where the key is it’s name and the value is the bean
     */
    @Override
    public <T> Map<String, T> getAllBeans(final Class<T> beanType) {
        return this.container.entrySet().stream()
                .filter(entry -> entry.getValue().getClass().isAssignableFrom(beanType))
                .collect(toMap(Map.Entry::getKey, e -> beanType.cast(e.getValue())));
    }

    private void initializeBeans(final String packageName) {
        var reflections = new Reflections(packageName);
        var targetClasses = reflections.getTypesAnnotatedWith(BoboComponent.class);
        for (var targetClass : targetClasses) {
            try {
                var constructor = targetClass.getConstructor();
                var beanObject = constructor.newInstance();
                var beanName = resolveBeanName(targetClass);
                if (container.containsKey(beanName)) {
                    throw new BeanNameIsNotUnique(String.format("Bean of type %s with name '%s' cannot be instantiated because its name is not unique!", beanObject.getClass().getName(), beanName));
                }
                this.container.put(beanName, beanObject);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void injectBeans() {
        for (var bean : container.values()) {
            final Class<?> beanClass = bean.getClass();
            final Field[] fields = beanClass.getDeclaredFields();
            for (var field : fields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    if (Modifier.isPrivate(field.getModifiers())) {
                        field.setAccessible(true);
                    }
                    final Object injectionCandidate = getBean(field.getType());
                    try {
                        field.set(bean, injectionCandidate);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
