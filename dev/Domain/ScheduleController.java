package Domain;

import Domain.Constraints;
import Domain.Employee;
import Domain.Role;
import Domain.ShiftRules;
import Domain.SuperMarket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
public class ScheduleController {
    private EmployeeController employeeManagement;
    private final String location;
    private final LocalDate startDate;
    private final LocalTime morningStart;
    private final LocalTime morningEnd;
    private final LocalTime eveningStart;
    private final LocalTime eveningEnd;
    private final SuperMarket superMarket;
    private final Map<Integer, ShiftRules> shiftRequirements = new HashMap<>();

    public ScheduleController(String location, LocalDate startDate, LocalTime morningStart, LocalTime morningEnd, LocalTime eveningStart, LocalTime eveningEnd, SuperMarket superMarket, EmployeeController employeeManagement) {
        this.location = location;
        this.startDate = startDate;
        this.morningStart = morningStart;
        this.morningEnd = morningEnd;
        this.eveningStart = eveningStart;
        this.eveningEnd = eveningEnd;
        this.superMarket = superMarket;
        this.employeeManagement = employeeManagement;

        // Initialize default shift requirements (0 employees and managers)
        for (int i = 0; i < 6; i++) {
            shiftRequirements.put(i, new ShiftRules(0, 0, 0, 0));
        }
    }

    public void setShiftRequirements(int day, ShiftRules rules) {
        shiftRequirements.put(day, rules);
    }

    public String generateWeeklySchedule(Map<String, Constraints> constraints) {
        StringBuilder schedule = new StringBuilder();
        schedule.append("Weekly Schedule:\n");

        for (int day = 0; day < 6; day++) {
            ShiftRules requirements = shiftRequirements.get(day);
            schedule.append("Day ").append(day + 1).append(":\n");
            schedule.append("  Morning Shift: ").append(assignShift(day, 0, requirements.getMorningManagerCount(), requirements.getMorningEmployeeCount(), constraints)).append("\n");
            schedule.append("  Evening Shift: ").append(assignShift(day, 1, requirements.getEveningManagerCount(), requirements.getEveningEmployeeCount(), constraints)).append("\n");
        }

        return schedule.toString();
    }

    private String assignShift(int day, int shiftType, int requiredManagers, int requiredEmployees, Map<String, Constraints> constraints) {
        List<String> assignedEmployeeNames = new ArrayList<>();
        int assignedManagers = 0;

        for (Map.Entry<String, Constraints> entry : constraints.entrySet()) {
            String employeeId = entry.getKey();
            Constraints constraint = entry.getValue();

            if (constraint.canWork(day, shiftType)) {
                Employee employee = employeeManagement.getEmployeeById(employeeId);
                if (employee.getRole() == Role.Manager && assignedManagers < requiredManagers) {
                    assignedEmployeeNames.add(employee.getFname());
                    assignedManagers++;
                } else if (employee.getRole() != Role.Manager && assignedEmployeeNames.size() - assignedManagers < requiredEmployees) {
                    assignedEmployeeNames.add(employee.getFname());
                }
            }

            if (assignedEmployeeNames.size() == requiredManagers + requiredEmployees) {
                break;
            }
        }
        return String.join(", ", assignedEmployeeNames);

    }


    public String getCurrentSchedule() {
        // Implement the logic to get the current schedule
        return "Current schedule not implemented yet.";
    }
}

