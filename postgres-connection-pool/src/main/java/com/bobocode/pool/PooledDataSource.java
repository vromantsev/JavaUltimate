package com.bobocode.pool;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PooledDataSource extends PGSimpleDataSource {

    private static final int POOL_SIZE = 10;

    private final Queue<Connection> pool;

    public PooledDataSource(final String url, final String username, final String password) {
        this.pool = new ConcurrentLinkedDeque<>();
        super.setURL(Objects.requireNonNull(url));
        super.setUser(Objects.requireNonNull(username));
        super.setPassword(Objects.requireNonNull(password));
        initializePool();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.pool.poll();
    }

    private void initializePool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                final Connection connection = new ConnectionProxy(super.getConnection(), this.pool);
                this.pool.add(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
