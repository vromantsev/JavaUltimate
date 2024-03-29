package com.bobocode.orm.session.impl;

import com.bobocode.orm.annotation.Column;
import com.bobocode.orm.annotation.Table;
import com.bobocode.orm.session.Session;
import com.bobocode.orm.session.queue.Action;
import com.bobocode.orm.session.queue.DeleleAction;
import com.bobocode.orm.session.queue.InsertAction;
import com.bobocode.orm.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Implementation of {@link Session}.
 */
@Log
@RequiredArgsConstructor
public class SimpleSession implements Session {

    private final DataSource dataSource;
    private final Queue<Action> actionQueue = new ConcurrentLinkedQueue<>();

    /**
     * {@inheritDoc}
     *
     * @param entityType entity type
     * @param id         entity id
     * @param <T>        generic type
     * @return entity from database
     */
    @Override
    public <T> T find(final Class<T> entityType, final Object id) {
        Objects.requireNonNull(entityType, "Parameter [entityType] must be provided!");
        Objects.requireNonNull(id, "Parameter [id] must be provided!");
        return executeQuery(this.dataSource, entityType, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        // no-op
    }

    @Override
    public <T> void persist(final T entity) {
        Objects.requireNonNull(entity, "Parameter [entity] must not be null!");
        var insertAction = new InsertAction(entity);
        this.actionQueue.add(insertAction);
    }

    @Override
    public <T> void remove(final T entity) {
        Objects.requireNonNull(entity, "Parameter [entity] must not be null!");
        var deleteAction = new DeleleAction(entity);
        this.actionQueue.add(deleteAction);
    }

    @Override
    public void flush() {
        this.actionQueue.stream()
                .sorted(Comparator.comparing(Action::getOrder))
                .forEach(this::fireAction);
    }

    private void fireAction(final Action action) {
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement ps = EntityUtils.preparedStatement(connection.prepareStatement(action.createSQL()), action)) {
            final int rowsUpdated = ps.executeUpdate();
            log.info(String.format("Executed action of type=%s, entity=%s. Updated %d row(s)", action.getActionType().name(), action.getEntity(), rowsUpdated));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Performs a jdbc-based call to db.
     *
     * @param dataSource current data source
     * @param entityType entity type
     * @param id         entity id
     * @param <T>        generic type
     * @return entity from db
     */
    private <T> T executeQuery(final DataSource dataSource, final Class<T> entityType, final Object id) {
        final String sql = getSQLQuery(entityType);
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, id);
            return createObject(ps.executeQuery(), entityType, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an object from a given result set.
     *
     * @param rs         result set
     * @param entityType entity type
     * @param id         entity id
     * @param <T>        generic type
     * @return created entity
     */
    private <T> T createObject(final ResultSet rs, final Class<T> entityType, final Object id) {
        try {
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
            return instance;
        } catch (SQLException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Allows to parse {@link Timestamp} to one of the given date-time types.
     *
     * @param field      entity field
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
     * Helps to compose a simple SQL query.
     *
     * @param entityType entity type
     * @param <T>        generic type
     * @return SQL query
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
        final Table table = entityType.getDeclaredAnnotation(Table.class);
        return String.format(sqlComposer.toString(), table.name());
    }
}
