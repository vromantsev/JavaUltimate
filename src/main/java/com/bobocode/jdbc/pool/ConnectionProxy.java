package com.bobocode.jdbc.pool;

import java.sql.Connection;

public class ConnectionProxy implements AutoCloseable {
    // todo: 1. store a physical connection and a pool reference
    // todo: 2. override method close
    // todo: 3. delegate all method invocations to a physical connection
    private Connection connection;

    public ConnectionProxy(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void close() throws Exception {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }
}
