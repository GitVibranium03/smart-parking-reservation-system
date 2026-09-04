package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public boolean insertUser(
            String name,
            String email,
            String password,
            String phone) {

        String sql = """
                INSERT INTO users
                (name, email, password, phone)
                VALUES (?, ?, ?, ?)
                """;

        try (
                Connection connection
                = DatabaseConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, phone);

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public boolean loginUser(String email, String password) {

        String sql = """
            SELECT user_id, name
            FROM users
            WHERE email = ?
            AND password = ?
            """;

        try (
                Connection connection
                = DatabaseConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                // System.out.println(
                //         "Login successful! Welcome "
                //         + resultSet.getString("name")
                // );
                return true;
            }

            // System.out.println("Invalid email or password.");
            return false;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        boolean result = userDAO.loginUser(
                "amit@gmail.com",
                "test123"
        );

        // boolean result = userDAO.insertUser(
        //         "vinay s",
        //         "vs@gmail.com",
        //         "test123",
        //         "4654865484"
        // );
        if (result) {
            System.out.println("Login successful!");
            // System.out.println("User registered successfully!");
        } else {
            System.out.println("Login failed!");
            // System.out.println("User registration failed!");
        }
    }
}
