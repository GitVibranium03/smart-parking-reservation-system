package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;

public class BookingDAO {

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
