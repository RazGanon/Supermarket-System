package Data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import Domain.Schedule;

public class ScheduleDAO {
    private static final String JDBC_URL = System.getenv("JDBC_URL");
    private Gson gson = new Gson();

    public void saveSchedule(int scheduleId, List<Schedule> schedules) {
        String jsonData = gson.toJson(schedules);
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            String sql = "INSERT INTO schedules (id, schedule) VALUES (?, ?) ON CONFLICT(id) DO UPDATE SET schedule = excluded.schedule";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, scheduleId);
                statement.setString(2, jsonData);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }

    public List<Schedule> loadSchedule(int scheduleId) {
        String jsonData = null;
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            String sql = "SELECT schedule FROM schedules WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, scheduleId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    jsonData = resultSet.getString("schedule");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
        Type listType = new TypeToken<List<Schedule>>() {}.getType();
        return gson.fromJson(jsonData, listType);
    }
}
