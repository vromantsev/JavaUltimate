package com.bobocode.orm.session;

/**
 * Interface for interactions with database.
 */
public interface Session {

    /**
     * Searches for an entity by a given identifier.
     *
     * @param entityType entity type
     * @param id         entity id
     * @param <T>        generic type
     * @return entity from database
     */
    <T> T find(final Class<T> entityType, Object id);

    /**
     * Releases session-related resources.
     */
    void close();

    <T> void persist(final T entity);

    <T> void remove(final T entity);

    void flush();

}
