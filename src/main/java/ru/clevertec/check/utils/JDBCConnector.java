package ru.clevertec.check.utils;

import ru.clevertec.check.exception.CheckRunnerException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.clevertec.check.config.DataSource.DATASOURCE_PASSWORD;
import static ru.clevertec.check.config.DataSource.DATASOURCE_URL;
import static ru.clevertec.check.config.DataSource.DATASOURCE_USERNAME;


public class JDBCConnector {
    private JDBCConnector() {
    }

    private static JDBCConnector instance;

    public static JDBCConnector getInstance() {
        if (instance == null) {
            instance = new JDBCConnector();
        }
        return instance;
    }

    private Connection connection;

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            if (connection == null || connection.isClosed()) {
                connect();
            }
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            throw CheckRunnerException.internalServerError();
        }
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(DATASOURCE_URL, DATASOURCE_USERNAME, DATASOURCE_PASSWORD);
        } catch (SQLException e) {
            throw CheckRunnerException.internalServerError();
        }
    }
}