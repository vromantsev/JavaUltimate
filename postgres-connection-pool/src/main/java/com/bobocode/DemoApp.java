package com.bobocode;

import com.bobocode.pool.DriverAgnosticPooledDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class DemoApp {

    @SneakyThrows
    public static void main(String[] args) {
        final PGSimpleDataSource pgDataSource = new PGSimpleDataSource();
        pgDataSource.setURL("jdbc:postgresql://localhost:5432/postgres");
        pgDataSource.setUser("ju22user");
        pgDataSource.setPassword("ju22pass");

        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL("jdbc:mysql://localhost:3306");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("mysql");

        var dataSource = initializePooledDataSource(mysqlDataSource);
        var total = 0.0;
        var start = System.nanoTime();
        for (int i = 0; i < 50; i++) {
            try (var connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                try (var statement = connection.createStatement()) {
                    var rs = statement.executeQuery("select rand() from test.products"); // use this for mysql
                    //var rs = statement.executeQuery("select random() from products"); // use this for postgres
                    rs.next();
                    total += rs.getDouble(1);
                }
                connection.rollback();
            }
        }
        System.out.println((System.nanoTime() - start) / 1000_000 + " ms");
        System.out.println(total);
    }

    private static DataSource initializePooledDataSource(DataSource dataSource) {
        return new DriverAgnosticPooledDataSource(dataSource);
    }
}
