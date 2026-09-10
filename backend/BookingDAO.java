package backend;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BookingDAO {

    public void getUserBookings(int userId) {

        String sql = """
            SELECT
                booking_id,
                vehicle_id,
                slot_id,
                booking_date,
                entry_time,
                exit_time,
                status
            FROM bookings
            WHERE user_id = ?
            ORDER BY booking_id DESC
            """;

        try (
                Connection connection
                = DatabaseConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            ResultSet resultSet
                    = statement.executeQuery();

            System.out.println("User Bookings:");

            while (resultSet.next()) {

                System.out.println(
                        resultSet.getInt("booking_id")
                        + " | Vehicle ID: "
                        + resultSet.getInt("vehicle_id")
                        + " | Slot ID: "
                        + resultSet.getInt("slot_id")
                        + " | Status: "
                        + resultSet.getString("status")
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public boolean createBooking(
            int userId,
            int vehicleId,
            int slotId,
            Date bookingDate,
            Timestamp entryTime,
            Timestamp exitTime) {

        String sql = """
                INSERT INTO bookings
                (user_id, vehicle_id, slot_id,
                 booking_date, entry_time, exit_time)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection
                = DatabaseConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setInt(2, vehicleId);
            statement.setInt(3, slotId);
            statement.setDate(4, bookingDate);
            statement.setTimestamp(5, entryTime);
            statement.setTimestamp(6, exitTime);

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }
}
