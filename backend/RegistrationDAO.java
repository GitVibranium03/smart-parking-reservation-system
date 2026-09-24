package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistrationDAO {

    public boolean registerUserWithVehicle(
            String name,
            String email,
            String password,
            String phone,
            String vehicleNumber,
            String vehicleType) {

        String userSql = """
                INSERT INTO users
                (name, email, password, phone)
                VALUES (?, ?, ?, ?)
                """;

        String userIdSql = """
                SELECT user_id
                FROM users
                WHERE email = ?
                """;

        String vehicleSql = """
                INSERT INTO vehicles
                (user_id, vehicle_number, vehicle_type)
                VALUES (?, ?, ?)
                """;

        Connection connection = null;

        try {

            connection =
                    DatabaseConnection.getConnection();

            // Start transaction
            connection.setAutoCommit(false);

            // 1. Insert user
            try (PreparedStatement userStatement =
                         connection.prepareStatement(userSql)) {

                userStatement.setString(1, name);
                userStatement.setString(2, email);
                userStatement.setString(3, password);
                userStatement.setString(4, phone);

                int rowsInserted =
                        userStatement.executeUpdate();

                if (rowsInserted == 0) {

                    connection.rollback();
                    return false;
                }
            }

            // 2. Get generated USER_ID
            int userId = -1;

            try (PreparedStatement userIdStatement =
                         connection.prepareStatement(userIdSql)) {

                userIdStatement.setString(1, email);

                try (ResultSet resultSet =
                             userIdStatement.executeQuery()) {

                    if (resultSet.next()) {

                        userId =
                                resultSet.getInt("user_id");
                    }
                }
            }

            if (userId == -1) {

                connection.rollback();
                return false;
            }

            // 3. Insert vehicle
            try (PreparedStatement vehicleStatement =
                         connection.prepareStatement(vehicleSql)) {

                vehicleStatement.setInt(1, userId);
                vehicleStatement.setString(
                        2,
                        vehicleNumber
                );

                vehicleStatement.setString(
                        3,
                        vehicleType
                );

                int rowsInserted =
                        vehicleStatement.executeUpdate();

                if (rowsInserted == 0) {

                    connection.rollback();
                    return false;
                }
            }

            // Everything succeeded
            connection.commit();

            return true;

        } catch (SQLException e) {

            // Rollback if anything fails
            if (connection != null) {

                try {

                    connection.rollback();

                } catch (SQLException rollbackError) {

                    rollbackError.printStackTrace();
                }
            }

            e.printStackTrace();

            return false;

        } finally {

            if (connection != null) {

                try {

                    // Restore default connection behavior
                    connection.setAutoCommit(true);

                    connection.close();

                } catch (SQLException closeError) {

                    closeError.printStackTrace();
                }
            }
        }
    }
}