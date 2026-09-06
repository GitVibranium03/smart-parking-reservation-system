package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VehicleDAO {

    public boolean addVehicle(
            int userId,
            String vehicleNumber,
            String vehicleType) {

        String sql = """
                INSERT INTO vehicles
                (user_id, vehicle_number, vehicle_type)
                VALUES (?, ?, ?)
                """;

        try (
                Connection connection
                = DatabaseConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, vehicleNumber);
            statement.setString(3, vehicleType);

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {

        VehicleDAO vehicleDAO = new VehicleDAO();

        boolean result = vehicleDAO.addVehicle(
                23,
                "MH12AB1234",
                "CAR"
        );

        if (result) {
            System.out.println("Vehicle added successfully!");
        } else {
            System.out.println("Vehicle addition failed!");
        }
    }
}
