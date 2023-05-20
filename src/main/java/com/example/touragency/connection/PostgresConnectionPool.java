package com.example.touragency.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class PostgresConnectionPool {

    private static PostgresConnectionPool instance = null;
    private final DataSource dataSource;

    private PostgresConnectionPool() {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/tour_agency_db");
        ds.setUsername("postgres");
        ds.setPassword("root");
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
        this.dataSource = ds;
    }

    public static PostgresConnectionPool getInstance() {
        if (instance == null) {
            synchronized (PostgresConnectionPool.class) {
                if (instance == null) {
                    instance = new PostgresConnectionPool();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
