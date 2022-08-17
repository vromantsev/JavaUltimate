package com.bobocode.jdbc.pool;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PooledDataSource extends PGSimpleDataSource {

    // store a queue of connections
    // init datasource with 10 connections
    // override getConnection method

    private static final int POOL_SIZE = 10;

    private final Queue<Connection> pool;

    public PooledDataSource(String url, String username, String password) {
        super.setURL(url);
        super.setUser(username);
        super.setPassword(password);
        pool = new ConcurrentLinkedDeque<>();
        initializePool();
    }

    private void initializePool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                pool.add(super.getConnection());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.pool.peek();
    }


}
