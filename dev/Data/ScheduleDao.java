package Data;

import Domain.Schedule;
import Domain.Shift;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDao {

    private static ScheduleDao single_instance = null;

    public ScheduleDao() {
    }

    public static synchronized ScheduleDao getInstance() {
        if (single_instance == null) {
            single_instance = new ScheduleDao();
        }
        return single_instance;
    }

    // Method to get all schedules
    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT DISTINCT week_number, supermarket_address FROM schedule";
        try (Connection conn = DataSource.openConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int weekNumber = rs.getInt("week_number");
                String supermarketAddress = rs.getString("supermarket_address");
                schedules.add(getSchedule(weekNumber, supermarketAddress));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    // Method to get a schedule by week number and supermarket address
    public Schedule getSchedule(int weekNumber, String supermarketAddress) {
        Schedule schedule = null;
        String query = "SELECT * FROM schedule WHERE week_number = ? AND supermarket_address = ?";
        try (Connection conn = DataSource.openConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, weekNumber);
            statement.setString(2, supermarketAddress);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    schedule = new Schedule(weekNumber, supermarketAddress, getShiftsForSchedule(conn, weekNumber, supermarketAddress));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedule;
    }

    // Method to get shifts for a schedule
    private List<Shift> getShiftsForSchedule(Connection conn, int weekNumber, String supermarketAddress) throws SQLException {
        List<Shift> shifts = new ArrayList<>();
        String query = "SELECT s.shift_id, s.day, s.period " +
                "FROM shift s " +
                "JOIN schedule_shift_employee sse ON s.shift_id = sse.shift_id " +
                "JOIN schedule sch ON sse.schedule_id = sch.rowid " +
                "WHERE sch.week_number = ? AND sch.supermarket_address = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, weekNumber);
            statement.setString(2, supermarketAddress);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    shifts.add(new Shift(rs.getString("day"), rs.getString("period")));
                }
            }
        }
        return shifts;
    }

    // Method to add a new schedule
    public void addSchedule(Schedule schedule, int weekNum) {
        String query = "INSERT INTO schedule (week_number, supermarket_address) VALUES (?, ?)";
        try (Connection conn = DataSource.openConnection();
             PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, weekNum);
            statement.setString(2, schedule.getSupermarketAddress());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int scheduleId = generatedKeys.getInt(1);
                    for (Shift shift : schedule.getShifts()) {
                        addShift(conn, scheduleId, shift);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add a shift to a schedule
    private void addShift(Connection conn, int scheduleId, Shift shift) throws SQLException {
        String shiftQuery = "INSERT INTO shift (day, period) VALUES (?, ?)";
        try (PreparedStatement shiftStatement = conn.prepareStatement(shiftQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            shiftStatement.setString(1, shift.getday());
            shiftStatement.setString(2, shift.getShiftType());
            shiftStatement.executeUpdate();

            try (ResultSet generatedKeys = shiftStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int shiftId = generatedKeys.getInt(1);
                    linkShiftToSchedule(conn, scheduleId, shiftId);
                }
            }
        }
    }

    // Method to link shift to schedule
    private void linkShiftToSchedule(Connection conn, int scheduleId, int shiftId) throws SQLException {
        String linkQuery = "INSERT INTO schedule_shift_employee (schedule_id, shift_id) VALUES (?, ?)";
        try (PreparedStatement linkStatement = conn.prepareStatement(linkQuery)) {
            linkStatement.setInt(1, scheduleId);
            linkStatement.setInt(2, shiftId);
            linkStatement.executeUpdate();
        }
    }
}
