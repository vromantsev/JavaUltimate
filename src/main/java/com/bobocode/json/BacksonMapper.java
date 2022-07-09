package com.bobocode.json;

import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BacksonMapper {

    private static final Logger LOGGER = Logger.getLogger(BacksonMapper.class.getName());

    @SneakyThrows
    public <T> T readObject(final String json, final Class<T> type) {
        final Constructor<T> constructor = type.getDeclaredConstructor();
        final T obj = constructor.newInstance();
        final Field[] fields = obj.getClass().getDeclaredFields();

        for (final Field field : fields) {
            if (field.isAnnotationPresent(PropertyName.class)) {
                processAnnotatedField(obj, field, json);
            } else {
                final List<String> lines = transformJson(json, true);
                field.setAccessible(true);
                final Iterator<String> iterator = lines.iterator();
                while (iterator.hasNext()) {
                    final String line = iterator.next();
                    final String[] values = line.split(":");
                    if (field.getName().equalsIgnoreCase(values[0])) {
                        field.set(obj, values[1]);
                        break;
                    }
                }
            }
        }
        return obj;
    }

    @SneakyThrows
    private <T> void processAnnotatedField(final T instance, final Field field, final String json) {
        final List<String> lines = transformJson(json, false);
        final PropertyName annotation = field.getDeclaredAnnotation(PropertyName.class);
        final String name = annotation.name();
        LOGGER.log(Level.INFO, String.format("Processing an annotated field %s mapped as %s", field.getName(), name));
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Field name must be provided, but found " + name);
        }
        for (final String line : lines) {
            if (line.contains(name)) {
                final String[] values = line.split(":");
                field.setAccessible(true);
                field.set(instance, values[1]);
            }
        }
    }

    private List<String> transformJson(String json, boolean stripSymbol) {
        json = json.substring(1, json.length() - 1);
        json = json.replaceAll("\"", "")
                .replaceAll("\n", "")
                .replaceAll(" ", "");
        if (stripSymbol) {
            json = json.replaceAll("_", "");
        }
        return Arrays.stream(json.split(",")).collect(Collectors.toList());
    }

    @Data
    public static class Person {
        @PropertyName(name = "first_name")
        private String firstName;
        private String lastName;
        private String city;
    }
}