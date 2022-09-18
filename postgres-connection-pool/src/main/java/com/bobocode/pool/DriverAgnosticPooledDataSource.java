package com.bobocode.pool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DriverAgnosticPooledDataSource extends NoOperationDataSource {

    private static final int POOL_SIZE = 10;

    private final Queue<Connection> pool;
    private final DataSource dataSource;

    public DriverAgnosticPooledDataSource(final DataSource dataSource) {
        this.dataSource = Objects.requireNonNull(dataSource, "Parameter [dataSource] must be provided!");
        this.pool = new ConcurrentLinkedDeque<>();
        initializePool();
    }

    @Override
    public Connection getConnection() {
        return this.pool.poll();
    }

    private void initializePool() {
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                final Connection connection = new ConnectionProxy(this.dataSource.getConnection(), this.pool);
                this.pool.add(connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
