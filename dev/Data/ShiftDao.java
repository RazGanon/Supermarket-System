package Data;

import Domain.Shift;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShiftDao {

    private static ShiftDao single_instance = null;

    public ShiftDao() {
    }

    public static synchronized ShiftDao getInstance() {
        if (single_instance == null) {
            single_instance = new ShiftDao();
        }
        return single_instance;
    }

    // Method to get all shifts
    public List<Shift> getAllShifts() {
        List<Shift> shifts = new ArrayList<>();
        String query = "SELECT * FROM shift";
        try (Connection conn = DataSource.openConnection();
             PreparedStatement statement = conn.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int shiftId = rs.getInt("id");
                String day = rs.getString("day");
                String period = rs.getString("period");
                Shift shift = new Shift( day, period,shiftId);
                shifts.add(shift);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shifts;
    }

    // Method to get shifts by day
    public List<Shift> getShiftsByDay(String day) {
        List<Shift> shifts = new ArrayList<>();
        String query = "SELECT * FROM shift WHERE day = ?";
        try (Connection conn = DataSource.openConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, day);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int shiftId = rs.getInt("shift_id");
                String period = rs.getString("period");
                Shift shift = new Shift( day, period,shiftId);
                shifts.add(shift);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shifts;
    }

    // Method to add a new shift
    public void addShift(Shift shift) {
        String query = "INSERT INTO shift (day, period) VALUES (?, ?)";
        try (Connection conn = DataSource.openConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, shift.getday());
            statement.setString(2, shift.getShiftType());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a shift
    public void updateShift(Shift shift) {
        String query = "UPDATE shift SET day = ?, period = ? WHERE shift_id = ?";
        try (Connection conn = DataSource.openConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, shift.getday());
            statement.setString(2, shift.getShiftType());
            statement.setInt(3, shift.getShiftid());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a shift
    public void deleteShift(int shiftId) {
        String query = "DELETE FROM shift WHERE shift_id = ?";
        try (Connection conn = DataSource.openConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, shiftId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
