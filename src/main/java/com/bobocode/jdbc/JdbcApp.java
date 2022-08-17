package com.bobocode.jdbc;

import com.bobocode.jdbc.pool.PooledDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class JdbcApp {

    public static void main(String[] args) throws SQLException {
        var dataSource = initializeDataSource();
        var total = 0.0;
        var start = System.nanoTime();
        for (int i = 0; i < 20; i++) {
            try (var connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                try (var statement = connection.createStatement()) {
                    var rs = statement.executeQuery("select random() from products");
                    rs.next();
                    total += rs.getDouble(1);
                }
                connection.rollback();
            }
        }
        System.out.println((System.nanoTime() - start) / 1000_000 + " ms");
        System.out.println(total);
    }

    private static DataSource initializeDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://93.175.204.87:5432/postgres");
        dataSource.setUser("ju22user");
        dataSource.setPassword("ju22pass");
        return dataSource;
    }

    private static DataSource initializePooledDataSource() {
        return new PooledDataSource(
                "jdbc:postgresql://93.175.204.87:5432/postgres",
                "ju22user",
                "ju22pass"
        );
    }
}
