package backend;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BookingDAO {

    public static void main(String[] args) {

        BookingDAO bookingDAO = new BookingDAO();

        boolean result = bookingDAO.createBooking(
                22,
                6,
                7,
                Date.valueOf("2026-09-13"),
                Timestamp.valueOf("2026-09-13 15:00:00"),
                Timestamp.valueOf("2026-09-13 16:00:00")
        );

        if (result) {
            System.out.println("Booking created successfully!");
        } else {
            System.out.println("Booking creation failed!");
        }
    }

    public boolean isSlotAvailable(int slotId) {

        String sql = """
            SELECT status
            FROM parking_slots
            WHERE slot_id = ?
            """;

        try (
                Connection connection
                = DatabaseConnection.getConnection(); PreparedStatement statement
                = connection.prepareStatement(sql)) {

            statement.setInt(1, slotId);

            ResultSet resultSet
                    = statement.executeQuery();

            if (resultSet.next()) {
                return "AVAILABLE".equals(
                        resultSet.getString("status")
                );
            }

            return false;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

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

        String bookingSql = """
            INSERT INTO bookings
            (user_id, vehicle_id, slot_id,
             booking_date, entry_time, exit_time)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        String slotSql = """
            UPDATE parking_slots
            SET status = 'OCCUPIED'
            WHERE slot_id = ?
            AND status = 'AVAILABLE'
            """;

        Connection connection = null;

        try {

            connection = DatabaseConnection.getConnection();

            // Start transaction
            connection.setAutoCommit(false);

            // 1. Check slot availability
            String checkSql = """
                SELECT status
                FROM parking_slots
                WHERE slot_id = ?
                FOR UPDATE
                """;

            try (PreparedStatement checkStatement
                    = connection.prepareStatement(checkSql)) {

                checkStatement.setInt(1, slotId);

                ResultSet resultSet
                        = checkStatement.executeQuery();

                if (!resultSet.next()
                        || !"AVAILABLE".equals(
                                resultSet.getString("status"))) {

                    connection.rollback();
                    return false;
                }
            }

            // 2. Create booking
            try (PreparedStatement bookingStatement
                    = connection.prepareStatement(bookingSql)) {

                bookingStatement.setInt(1, userId);
                bookingStatement.setInt(2, vehicleId);
                bookingStatement.setInt(3, slotId);
                bookingStatement.setDate(4, bookingDate);
                bookingStatement.setTimestamp(5, entryTime);
                bookingStatement.setTimestamp(6, exitTime);

                bookingStatement.executeUpdate();
            }

            // 3. Mark slot as occupied
            try (PreparedStatement slotStatement
                    = connection.prepareStatement(slotSql)) {

                slotStatement.setInt(1, slotId);

                slotStatement.executeUpdate();
            }

            // 4. Commit both operations
            connection.commit();

            return true;

        } catch (SQLException e) {

            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }

            e.printStackTrace();
            return false;

        } finally {

            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
