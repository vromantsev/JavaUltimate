package com.bobocode.comparator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * A generic comparator that is comparing a random field of the given class. The field is either primitive or
 * {@link Comparable}. It is chosen during comparator instance creation and is used for all comparisons.
 * <p>
 * By default it compares only accessible fields, but this can be configured via a constructor property. If no field is
 * available to compare, the constructor throws {@link IllegalArgumentException}
 *
 * @param <T> the type of the objects that may be compared by this comparator
 */
public class RandomFieldComparator<T> implements Comparator<T> {

    private final Class<T> targetType;
    private final Field randomField;
    private final ThreadLocalRandom threadLocalRandom;

    {
        threadLocalRandom = ThreadLocalRandom.current();
    }

    public RandomFieldComparator(Class<T> targetType) {
        this(targetType, true);
    }

    /**
     * A constructor that accepts a class and a property indicating which fields can be used for comparison. If property
     * value is true, then only public fields or fields with public getters can be used.
     *
     * @param targetType                  a type of objects that may be compared
     * @param compareOnlyAccessibleFields config property indicating if only publicly accessible fields can be used
     */
    public RandomFieldComparator(Class<T> targetType, boolean compareOnlyAccessibleFields) {
        Objects.requireNonNull(targetType, "Target class must be provided!");
        this.targetType = targetType;
        final List<Field> fields = getFields(targetType, compareOnlyAccessibleFields);
        if (fields.isEmpty()) {
            throw new IllegalArgumentException("No fields are available for comparing!");
        }
        this.randomField = fields.get(this.threadLocalRandom.nextInt(fields.size()));
    }

    /**
     * Returns a list of field that are either primitive or Comparable.
     *
     * @param targetType                  target class
     * @param compareOnlyAccessibleFields if true only public fields or fields that have public getters should be included,
     *                                    otherwise another filter criteria is applied
     * @return filtered list of fields
     */
    private List<Field> getFields(final Class<T> targetType, final boolean compareOnlyAccessibleFields) {
        final List<Field> fields = new ArrayList<>();
        final Field[] declaredFields = targetType.getDeclaredFields();
        if (compareOnlyAccessibleFields) {
            final Method[] methods = targetType.getMethods();
            for (final Field field : declaredFields) {
                final boolean isMatched = FIELD_PREDICATE.test(field);
                if (Modifier.isPublic(field.getModifiers()) && isMatched) {
                    fields.add(field);
                } else {
                    Arrays.stream(methods)
                            .filter(m -> METHOD_NAME_PREDICATE.test(field, m))
                            .findFirst()
                            .ifPresent(m -> {
                                if (isMatched) {
                                    field.setAccessible(true);
                                    fields.add(field);
                                }
                            });
                }
            }
        } else {
            Arrays.stream(declaredFields)
                    .filter(FIELD_PREDICATE)
                    .peek(field -> field.setAccessible(true))
                    .forEach(fields::add);
        }
        return fields;
    }

    /**
     * Checks whether the field type is primitive or Comparable.
     */
    private static final Predicate<Field> FIELD_PREDICATE =
            field -> field.getType().isPrimitive() || Comparable.class.isAssignableFrom(field.getType());

    /**
     * BiPredicate compares that a given method name is matched a manually composed method name.
     */
    private static final BiPredicate<Field, Method> METHOD_NAME_PREDICATE = (f, m) ->
            m.getName().equalsIgnoreCase(getGetterName("get", f.getName()))
                    || m.getName().equalsIgnoreCase(getGetterName("is", f.getName()));

    /**
     * Composes a full getter name.
     *
     * @param prefix    method prefix e.g. 'get' or 'is'
     * @param fieldName field name
     * @return full getter name
     */
    private static String getGetterName(final String prefix, final String fieldName) {
        return prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    /**
     * Compares two objects of the class T by the value of the field that was randomly chosen. It allows null values
     * for the fields, and it treats null value grater than a non-null value (nulls last).
     */
    @Override
    public int compare(T o1, T o2) {
        Objects.requireNonNull(o1, "First argument should be not null!");
        Objects.requireNonNull(o2, "Second argument should be not null!");
        try {
            @SuppressWarnings("rawtypes") final Comparable first = (Comparable) randomField.get(o1);
            @SuppressWarnings("rawtypes") final Comparable second = (Comparable) randomField.get(o2);
            @SuppressWarnings("unchecked") final int result = first.compareTo(second);
            return result;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Returns a statement "Random field comparator of class '%s' is comparing '%s'" where the first param is the name
     * of the type T, and the second parameter is the comparing field name.
     *
     * @return a predefined statement
     */
    @Override
    public String toString() {
        return String.format("Random field comparator of class '%s' is comparing '%s'", this.targetType.getName(), this.randomField.getName());
    }
}