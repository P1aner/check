package ru.clevertec.check.utils;

import ru.clevertec.check.exception.CheckRunnerException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.clevertec.check.config.AppProperties.datasourcePassword;
import static ru.clevertec.check.config.AppProperties.datasourceUrl;
import static ru.clevertec.check.config.AppProperties.datasourceUsername;


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
            if (connection == null || connection.isClosed()) {
                connect();
            }
            return connection;
        } catch (SQLException e) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(datasourceUrl, datasourceUsername, datasourcePassword);
        } catch (SQLException e) {
            throw new CheckRunnerException("INTERNAL SERVER ERROR");
        }
    }
}