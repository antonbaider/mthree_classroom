package org.example.configs;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

import static org.example.configs.LoggerConfig.getLogger;

public class DBConfig {
    private static final Logger logger = getLogger();
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DBConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(input);
        } catch (IOException e) {
            logger.severe("Failed to load database configuration: " + e.getMessage());
        }
    }

    private static final String URL = properties.getProperty("db.url");
    private static final String USER = properties.getProperty("db.user");
    private static final String PASSWORD = properties.getProperty("db.password");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static PreparedStatement startConnection(String sql) throws SQLException {
        Connection connection = getConnection();
        logger.info("Executing SQL: " + sql);
        return connection.prepareStatement(sql);
    }

}
