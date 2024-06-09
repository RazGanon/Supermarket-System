package Domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Shift {
    private SuperMarket branch;
    private List<String> availableEmployees; // Stores the IDs of employees who have marked themselves as available for this shift
    private Map<Role, List<String>> employees;
    private List<Integer> managerConstraints;
    private boolean confirmed;
    private ShiftType type; // Morning/evening
    private DayShift day;

    /**
     * Constructor for the Shift class.
     *
     * @param branch the supermarket branch where the shift is scheduled
     * @param type   the type of shift (morning/evening)
     * @param day    the day of the shift
     */
    public Shift(SuperMarket branch, ShiftType type, DayShift day) {
        if (branch == null || type == null || day == null) {
            throw new IllegalArgumentException("Branch, type, and day cannot be null");
        }
        this.availableEmployees = new LinkedList<>();
        this.employees = new HashMap<>();
        this.type = type;
        this.confirmed = false;
        this.branch = branch;
        this.managerConstraints = new LinkedList<>();
        this.day = day;
    }

    /**
     * Adds an employee to the list of those available for the shift.
     *
     * @param employeeId the ID of the employee to add
     */
    public void addEmployeeAvailability(String employeeId) {
        if (!availableEmployees.contains(employeeId)) { // Check if employee is already in availableEmployees
            availableEmployees.add(employeeId);
        }
    }

    /**
     * Checks if an employee is available for the shift.
     *
     * @param employeeId the ID of the employee to check
     * @return true if the employee is available, false otherwise
     */
    public boolean isEmployeeAvailable(String employeeId) {
        return availableEmployees.contains(employeeId);
    }

    @Override
    public String toString() {
        return "Shift{" +
                "superMarketBranch=" + branch +
                ", type=" + type +
                ", day=" + day +
                ", availableEmployees=" + availableEmployees +
                ", confirmed=" + confirmed +
                ", managerConstraints=" + managerConstraints +
                '}';
    }
}
