package com.bobocode.orm.session;

import com.bobocode.orm.annotation.Column;
import com.bobocode.orm.annotation.Table;
import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Implementation of {@link Session}.
 */
@RequiredArgsConstructor
public class SessionImpl implements Session {

    private static final Logger LOGGER = Logger.getLogger(SessionImpl.class.getSimpleName());

    private static final Map<String, Object> ENTITY_CACHE = new ConcurrentHashMap<>();

    private final DataSource dataSource;

    /**
     * {@inheritDoc}
     *
     * @param entityType entity type
     * @param id entity id
     * @return entity from database
     * @param <T>
     */
    @Override
    public <T> T find(final Class<T> entityType, final Object id) {
        Objects.requireNonNull(entityType, "Parameter [entityType] must be provided!");
        Objects.requireNonNull(id, "Parameter [id] must be provided!");
        @SuppressWarnings("unchecked") final T cachedEntity = (T) ENTITY_CACHE.getOrDefault(String.valueOf(id), null);
        if (cachedEntity != null) {
            LOGGER.info(String.format("Getting entity with id=%s from cache.", id));
            return cachedEntity;
        } else {
            return doWithJDBC(this.dataSource, entityType, id);
        }
    }

    /**
     * Performs a jdbc-based call to db.
     *
     * @param dataSource current data source
     * @param entityType entity type
     * @param id entity id
     * @return entity from db
     * @param <T> generic type
     */
    private <T> T doWithJDBC(final DataSource dataSource, final Class<T> entityType, final Object id) {
        final String tableName = getTableName(entityType);
        final String sql = String.format(getSQLQuery(entityType), tableName);
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            final ResultSet rs = ps.executeQuery();
            final T instance = entityType.getDeclaredConstructor().newInstance();
            final Field[] fields = instance.getClass().getDeclaredFields();
            if (rs.next()) {
                for (final Field field : fields) {
                    if (Modifier.isPrivate(field.getModifiers())) {
                        field.setAccessible(true);
                    }
                    Object fieldValue = rs.getObject(field.getAnnotation(Column.class).name());
                    if (fieldValue instanceof Timestamp) {
                        fieldValue = adaptTimestamp(field, fieldValue);
                    }
                    field.set(instance, fieldValue);
                }
            }
            LOGGER.info(String.format("Caching entity with id=%s", id));
            ENTITY_CACHE.put(String.valueOf(id), instance);
            return instance;
        } catch (SQLException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Allows to parse {@link Timestamp} to one of the given date-time types.
     *
     * @param field entity field
     * @param fieldValue field value retrieved from db
     * @return adapted date/date-time/time value
     */
    private Object adaptTimestamp(final Field field, Object fieldValue) {
        if (field.getType().isAssignableFrom(LocalDateTime.class)) {
            fieldValue = ((Timestamp) fieldValue).toLocalDateTime();
        } else if (field.getType().isAssignableFrom(LocalDate.class)) {
            fieldValue = ((Timestamp) fieldValue).toLocalDateTime().toLocalDate();
        } else if (field.getType().isAssignableFrom(LocalTime.class)) {
            fieldValue = ((Timestamp) fieldValue).toLocalDateTime().toLocalTime();
        } else if (field.getType().isAssignableFrom(java.util.Date.class)) {
            fieldValue = java.util.Date.from(((Timestamp) fieldValue).toInstant());
        } else if (field.getType().isAssignableFrom(java.sql.Date.class)) {
            fieldValue = java.sql.Date.from(((Timestamp) fieldValue).toInstant());
        }
        return fieldValue;
    }

    /**
     * Extracts a table name from an entity.
     *
     * @param entityType entity type
     * @return table name
     * @param <T> generic type
     */
    private <T> String getTableName(Class<T> entityType) {
        final Table table = entityType.getDeclaredAnnotation(Table.class);
        final String tableName = table.name();
        return tableName;
    }

    /**
     * Helps to compose a simple SQL query.
     *
     * @param entityType entity type
     * @return SQL query
     * @param <T> generic type
     */
    private <T> String getSQLQuery(final Class<T> entityType) {
        var sqlComposer = new StringBuilder();
        sqlComposer.append("SELECT ");
        final Field[] fields = entityType.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(Column.class)) {
                final Column column = fields[i].getDeclaredAnnotation(Column.class);
                final String columnName = column.name();
                sqlComposer.append(columnName);
            }
            if (i < fields.length - 1) {
                sqlComposer.append(", ");
            }
        }
        sqlComposer.append(" FROM test.%s WHERE id = ?");
        return sqlComposer.toString();
    }
}
