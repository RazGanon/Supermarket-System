package Data;

import Domain.Constraints;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConstraintsDao {
    public ConstraintsDao(){}

    public Constraints getConstraintsByEmployeeId(String employeeId) {
        String sql = "SELECT * FROM ConstraintsTable WHERE Id = ?";
        System.out.println("Executing query: " + sql + " with Id: " + employeeId);

        try (Connection conn = DataSource.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int[][] constraintsMatrix = new int[2][6];

                constraintsMatrix[0][0] = rs.getInt("`Day 1 M`");
                constraintsMatrix[1][0] = rs.getInt("`Day 1 E`");
                constraintsMatrix[0][1] = rs.getInt("`Day 2 M`");
                constraintsMatrix[1][1] = rs.getInt("`Day 2 E`");
                constraintsMatrix[0][2] = rs.getInt("`Day 3 M`");
                constraintsMatrix[1][2] = rs.getInt("`Day 3 E`");
                constraintsMatrix[0][3] = rs.getInt("`Day 4 M`");
                constraintsMatrix[1][3] = rs.getInt("`Day 4 E`");
                constraintsMatrix[0][4] = rs.getInt("`Day 5 M`");
                constraintsMatrix[1][4] = rs.getInt("`Day 5 E`");
                constraintsMatrix[0][5] = rs.getInt("`Day 6 M`");
                constraintsMatrix[1][5] = rs.getInt("`Day 6 E`");

                return new Constraints(constraintsMatrix);
            } else {
                System.out.println("No constraints found for employee ID: " + employeeId);
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void saveConstraints(String employeeId, Constraints constraints,int weekNum) {
        String sql = "INSERT INTO ConstraintsTable (week,Id, `Day 1 M`, `Day 1 E`, `Day 2 M`, `Day 2 E`, "
                + "`Day 3 M`, `Day 3 E`, `Day 4 M`, `Day 4 E`, `Day 5 M`, `Day 5 E`, `Day 6 M`, `Day 6 E`) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        System.out.println("Executing query: " + sql + " with Id: " + employeeId);

        try (Connection conn = DataSource.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employeeId);
            int[][] matrix = constraints.getMatrix();
            for (int day = 0; day < 6; day++) {
                pstmt.setInt(2 + day * 2, matrix[0][day]);
                pstmt.setInt(3 + day * 2, matrix[1][day]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void printTable() {
        String sql = "SELECT * FROM ConstraintsTable";

        try (Connection conn = DataSource.openConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("Id");
                int day1M = rs.getInt("Day 1 M");
                int day1E = rs.getInt("Day 1 E");
                int day2M = rs.getInt("Day 2 M");
                int day2E = rs.getInt("Day 2 E");
                int day3M = rs.getInt("Day 3 M");
                int day3E = rs.getInt("Day 3 E");
                int day4M = rs.getInt("Day 4 M");
                int day4E = rs.getInt("Day 4 E");
                int day5M = rs.getInt("Day 5 M");
                int day5E = rs.getInt("Day 5 E");
                int day6M = rs.getInt("Day 6 M");
                int day6E = rs.getInt("Day 6 E");

                System.out.println("ID: " + id +
                        ", Day 1 M: " + day1M + ", Day 1 E: " + day1E +
                        ", Day 2 M: " + day2M + ", Day 2 E: " + day2E +
                        ", Day 3 M: " + day3M + ", Day 3 E: " + day3E +
                        ", Day 4 M: " + day4M + ", Day 4 E: " + day4E +
                        ", Day 5 M: " + day5M + ", Day 5 E: " + day5E +
                        ", Day 6 M: " + day6M + ", Day 6 E: " + day6E);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        ConstraintsDao dao = new ConstraintsDao();

        // Print the entire table to verify the data
        dao.printTable();

        // Test: Save and retrieve constraints
        testSaveAndRetrieveConstraints(dao, "208398511");

        // Close the connection
        DataSource.closeConnection();
    }

    private static void testSaveAndRetrieveConstraints(ConstraintsDao dao, String employeeId) {
        // Create a sample constraints matrix
        int[][] sampleMatrix = {
                {1, 0, 1, 0, 1, 0}, // Morning constraints
                {0, 1, 0, 1, 0, 1}  // Evening constraints
        };
        Constraints sampleConstraints = new Constraints(sampleMatrix);

        // Save the sample constraints to the database
        dao.saveConstraints(employeeId, sampleConstraints);

        // Retrieve and print the constraints to verify
        Constraints retrievedConstraints = dao.getConstraintsByEmployeeId(employeeId);
        if (retrievedConstraints != null) {
            int[][] matrix = retrievedConstraints.getMatrix();
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }
        }
    }
}