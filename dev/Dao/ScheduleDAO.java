package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScheduleDAO {
    // Database credentials
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/yourdatabase";
    private static final String USERNAME = "yourusername";
    private static final String PASSWORD = "yourpassword";

    // Save schedule to the database
    public void saveSchedule(int scheduleId, String schedule) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO schedules (id, schedule) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, scheduleId);
            statement.setString(2, schedule);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }

    // Load schedule from the database
    public String loadSchedule(int scheduleId) {
        StringBuilder schedule = new StringBuilder();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT schedule FROM schedules WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, scheduleId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                schedule.append(resultSet.getString("schedule"));
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
        return schedule.toString();
    }
}
