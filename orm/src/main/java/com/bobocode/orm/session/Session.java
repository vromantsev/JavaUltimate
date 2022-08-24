package com.bobocode.orm.session;

/**
 * Interface for interactions with database.
 */
public interface Session {

    /**
     * Searches for an entity by a given identifier.
     *
     * @param entityType entity type
     * @param id entity id
     * @return entity from database
     * @param <T> generic type
     */
    <T> T find(final Class<T> entityType, Object id);

    /**
     * Releases session-related resources.
     */
    void close();

}
