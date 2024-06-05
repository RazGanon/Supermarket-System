package Domain;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Shift {
    private SuperMarket branch;
    private List<String> available_employees; //stores the IDs of employees who have marked themselves as available for this shift.
    private Map<Role, List<String>> employees;
    private List<Integer> manager_constraints;
    private boolean confirmed;
    private ShiftType type; // morning/evening
    private DayShift day;

    public Shift(SuperMarket branch, ShiftType type, DayShift day) {
        available_employees = new LinkedList<>();
        employees = new HashMap<>();
        this.type = type;
        confirmed = false;
        this.branch = branch;
        manager_constraints = new LinkedList<>();
        this.day = day;
    }

    public void addEmployeeAvailability(String employeeId) {
        if (!available_employees.contains(employeeId)) { // check if employee is already in available_employees
            available_employees.add(employeeId);
        }
    }

    public boolean isEmployeeAvailable(String employeeId) {
        return available_employees.contains(employeeId);
    }

    @Override
    public String toString() {
        return "Shift{" +
                "branch=" + branch +
                ", type=" + type +
                ", day=" + day +
                ", availableEmployees=" + available_employees +
                '}';
    }
}