package com.bobocode.orm;

import com.bobocode.orm.entity.Product;
import com.bobocode.orm.session.Session;
import com.bobocode.orm.session.factory.SessionFactory;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.time.LocalDateTime;

public class DemoApp {
    public static void main(String[] args) {
        DataSource mysqlDataSource = initializeDataSource();
        var sf = new SessionFactory(mysqlDataSource);
        final Session session = sf.createSession();
        //final Session session = sf.createCacheableSession();

        Product newProduct = new Product();
        newProduct.setName("Bayraktar");
        newProduct.setPrice(1000_000);
        newProduct.setCreatedAt(LocalDateTime.now());
        newProduct.setId(5L);
        session.remove(newProduct);
        //session.persist(newProduct);

        session.flush();
    }

    private static DataSource initializeDataSource() {
        final MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL("jdbc:mysql://localhost:3306");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("mysql");
        return mysqlDataSource;
    }
}
