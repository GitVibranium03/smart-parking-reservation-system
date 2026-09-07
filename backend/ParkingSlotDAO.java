package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParkingSlotDAO {

    public void getAvailableSlots() {

        String sql = """
                SELECT slot_id, slot_number, vehicle_type
                FROM parking_slots
                WHERE status = ?
                ORDER BY slot_id
                """;

        try (
                Connection connection
                = DatabaseConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setString(1, "AVAILABLE");

            ResultSet resultSet
                    = statement.executeQuery();

            System.out.println("Available Parking Slots:");

            while (resultSet.next()) {

                int slotId
                        = resultSet.getInt("slot_id");

                String slotNumber
                        = resultSet.getString("slot_number");

                String vehicleType
                        = resultSet.getString("vehicle_type");

                System.out.println(
                        slotId + " | "
                        + slotNumber + " | "
                        + vehicleType
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ParkingSlotDAO parkingSlotDAO
                = new ParkingSlotDAO();

        parkingSlotDAO.getAvailableSlots();
    }
}
