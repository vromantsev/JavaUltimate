package com.bobocode.orm;

import com.bobocode.orm.entity.Product;
import com.bobocode.orm.session.Session;
import com.bobocode.orm.session.factory.SessionFactory;
import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

public class DemoApp {
    public static void main(String[] args) {
        DataSource mysqlDataSource = initializeDataSource();
        var sf = new SessionFactory(mysqlDataSource);
        final Session session = sf.createSession();
        final Product product = session.find(Product.class, 11L);
        System.out.println(product);

        final Product p = session.find(Product.class, 11L);
        System.out.println(p);

        System.out.println(product == p);
    }

    private static DataSource initializeDataSource() {
        final MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL("jdbc:mysql://localhost:3306");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("mysql");
        return mysqlDataSource;
    }
}
