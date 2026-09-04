package backend;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String CONFIG_FILE = "config.properties";

    public static Connection getConnection() throws SQLException {

        Properties properties = new Properties();

        try (FileInputStream input =
                     new FileInputStream(CONFIG_FILE)) {

            properties.load(input);

        } catch (IOException e) {

            throw new SQLException(
                    "Could not load database configuration.",
                    e
            );
        }

        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        return DriverManager.getConnection(
                url,
                username,
                password
        );
    }
}
