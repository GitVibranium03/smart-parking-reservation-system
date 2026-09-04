package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    public static void main(String[] args) {

        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "SCOTT";
        String password = "TIGER";

        try {

            Connection connection
                    = DriverManager.getConnection(url, username, password);

            System.out.println("Database connected successfully!");

            String sql = "SELECT * FROM users";

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("\nUsers:");

            while (resultSet.next()) {

                int userId = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");

                System.out.println(
                        userId + " | "
                        + name + " | "
                        + email + " | "
                        + phone
                );
            }

            resultSet.close();
            statement.close();
            connection.close();

            System.out.println("\nDatabase connection closed.");

        } catch (SQLException e) {

            System.out.println("Database operation failed!");

            e.printStackTrace();
        }
    }
}
