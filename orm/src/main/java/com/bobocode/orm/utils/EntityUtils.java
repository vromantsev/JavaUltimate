package com.bobocode.orm.utils;

import com.bobocode.orm.annotation.Column;
import com.bobocode.orm.annotation.Id;
import com.bobocode.orm.annotation.Table;
import com.bobocode.orm.session.queue.Action;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@UtilityClass
public class EntityUtils {

    public <T> List<String> getSortedColumnNames(final T entity) {
        final Class<?> entityClass = entity.getClass();
        final Field[] fields = entityClass.getDeclaredFields();
        final List<String> columns = new ArrayList<>(fields.length);
        for (final Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                final Column column = field.getAnnotation(Column.class);
                final String columnName = column.name();
                columns.add(columnName);
            }
        }
        return columns;
    }

    public <T> List<String> getSortedColumnNamesWithoutId(final T entity) {
        return EntityUtils.getSortedColumnNames(entity)
                .stream()
                .filter(Predicate.not(column -> column.equals("id")))
                .collect(Collectors.toList());
    }

    public String join(final List<String> columns) {
        return columns.stream().sorted().collect(Collectors.joining(","));
    }

    public String getTableName(final Class<?> entityClass) {
        return Optional.ofNullable(entityClass.getDeclaredAnnotation(Table.class))
                .map(t -> t.schema().isBlank() ? t.name() : t.schema() + "." + t.name())
                .orElseGet(() -> createTableNameFromClass(entityClass.getSimpleName()));
    }

    private String createTableNameFromClass(final String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    public <T> String getMappings(final List<T> columns) {
        return columns.stream().map(f -> "?").collect(Collectors.joining(","));
    }

    public <T> String getMappings(final T entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields()).map(f -> "?").collect(Collectors.joining(","));
    }

    public <T> Object getId(final T entity) {
        try {
            final Class<?> entityClass = entity.getClass();
            final Field[] fields = entityClass.getDeclaredFields();
            final Field field = Arrays.stream(fields)
                    .filter(f -> f.isAnnotationPresent(Id.class))
                    .findAny()
                    .orElseThrow(() -> new IllegalStateException(String.format("Class %s has field that is not marked via @Id annotation!", entityClass.getName())));
            field.setAccessible(true);
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> List<Field> sortEntityFields(final Class<T> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(Predicate.not(f -> f.isAnnotationPresent(Id.class)))
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }

    public <T> PreparedStatement preparedStatement(final PreparedStatement statement,
                                                   final Action action) {
        final T entity = (T) action.getEntity();
        final Class<?> entityClass = entity.getClass();
        switch (action.getActionType()) {
            case INSERT -> {
                final List<Field> fields = sortEntityFields(entityClass);
                int magicNumber = 1;
                for (final Field field : fields) {
                    try {
                        field.setAccessible(true);
                        statement.setObject(magicNumber++, field.get(entity));
                    } catch (SQLException | IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                }
            }
            case DELETE -> {
                int magicNumber = 1;
                try {
                    final Object id = EntityUtils.getId(entity);
                    statement.setObject(magicNumber, id);
                } catch (SQLException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        };
        return statement;
    }

    public <T> Field getIdField(final T entity) {
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(String.format("Class %s has field hat is not marked via @Id annotation!", entity.getClass().getName())));
    }

    public String getColumnName(final Field field) {
        return Optional.ofNullable(field.getDeclaredAnnotation(Column.class))
                .map(Column::name)
                .orElseGet(field::getName);
    }
}
