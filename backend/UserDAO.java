package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Existing registration method
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
                = DatabaseConnection.getConnection();

                PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, phone);

            int rowsInserted =
                    statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    // Registration method that returns generated USER_ID
    public int insertUserAndGetId(
            String name,
            String email,
            String password,
            String phone) {

        String insertSql = """
                INSERT INTO users
                (name, email, password, phone)
                VALUES (?, ?, ?, ?)
                """;

        String selectSql = """
                SELECT user_id
                FROM users
                WHERE email = ?
                """;

        try (
                Connection connection
                = DatabaseConnection.getConnection();

                PreparedStatement insertStatement
                = connection.prepareStatement(insertSql)) {

            insertStatement.setString(1, name);
            insertStatement.setString(2, email);
            insertStatement.setString(3, password);
            insertStatement.setString(4, phone);

            int rowsInserted =
                    insertStatement.executeUpdate();

            if (rowsInserted == 0) {
                return -1;
            }

            try (
                    PreparedStatement selectStatement
                    = connection.prepareStatement(selectSql)) {

                selectStatement.setString(1, email);

                ResultSet resultSet =
                        selectStatement.executeQuery();

                if (resultSet.next()) {

                    return resultSet.getInt("user_id");
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return -1;
    }


    // Login method
    public boolean loginUser(
            String email,
            String password) {

        String sql = """
                SELECT user_id, name
                FROM users
                WHERE email = ?
                AND password = ?
                """;

        try (
                Connection connection
                = DatabaseConnection.getConnection();

                PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                return true;
            }

            return false;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {

        UserDAO userDAO =
                new UserDAO();

        boolean result =
                userDAO.loginUser(
                        "amit@gmail.com",
                        "test123"
                );

        if (result) {

            System.out.println(
                    "Login successful!"
            );

        } else {

            System.out.println(
                    "Login failed!"
            );
        }
    }
}