package com.bobocode.orm.session.impl;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link com.bobocode.orm.session.Session} that uses caching.
 */
public class CacheableSession extends SimpleSession {

    private final Map<String, Object> entityCache = new HashMap<>();

    public CacheableSession(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * {@inheritDoc}
     *
     * @param entityType entity type
     * @param id         entity id
     * @param <T>        generic type
     * @return entity from cache
     */
    @Override
    public <T> T find(final Class<T> entityType, final Object id) {
        Objects.requireNonNull(entityType, "Parameter [entityType] must be provided!");
        Objects.requireNonNull(id, "Parameter [id] must be provided!");
        final Object result = this.entityCache.computeIfAbsent(String.valueOf(id), key -> super.find(entityType, id));
        return entityType.cast(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        this.entityCache.clear();
    }

    @Override
    public <T> void persist(T entity) {
        super.persist(entity);
    }

    @Override
    public <T> void remove(T entity) {
        super.remove(entity);
    }

    @Override
    public void flush() {
        super.flush();
    }
}
