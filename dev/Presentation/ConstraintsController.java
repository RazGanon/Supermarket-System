package Presentation;

import Domain.ShiftRules;
import Service.ConstraintsService;
import Service.ScheduleService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ConstraintsController {
    private final ConstraintsService constraintsService;
    private final ScheduleService scheduleService;

    public ConstraintsController(ConstraintsService constraintsService, ScheduleService scheduleService) {
        this.constraintsService = constraintsService;
        this.scheduleService = scheduleService;
    }
    public void stopSubmission(){
        constraintsService.stopSubmission();
    }
    public void StartSubmission(){
        constraintsService.startSubmission();
    }

    public void addConstraints(String employeeId, Scanner scanner) {
        if (constraintsService.getSubmission() == 1) {
            System.out.println("Submissions are currently not allowed.");
            return;
        }
        Map<Integer, String> userInputConstraints = new HashMap<>();
        for (int day = 0; day < 6; day++) {
            while (true) {
                System.out.println("Enter constraints for day " + (day + 1) + ": (11 for both, 10 for morning, 01 for evening, 00 for none)");
                String input = scanner.nextLine();
                if (validateInput(input)) {
                    userInputConstraints.put(day, input);
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid constraint (11, 10, 01, or 00).");
                }
            }
        }
        constraintsService.addConstraintsFromUserInput(employeeId, userInputConstraints);
        System.out.println("Constraints added for employee ID: " + employeeId);
    }

    private boolean validateInput(String input) {
        return input.equals("11") || input.equals("10") || input.equals("01") || input.equals("00");
    }
    public void printConstraintsForEmployee(String employeeId) {
        constraintsService.printConstraintsForEmployee(employeeId);
    }

    public void printAllConstraints() {
        constraintsService.printAllConstraints();
    }

    public void setShiftRequirements(Scanner scanner) {
        //first need to check if admin let the employee's eo append their requirements


        for (int day = 0; day < 6; day++) {
            System.out.println("Enter requirements for day " + (day + 1) + ":");
            System.out.print("Morning Managers: ");
            int morningManagers = scanner.nextInt();
            System.out.print("Morning Employees: ");
            int morningEmployees = scanner.nextInt();
            System.out.print("Evening Managers: ");
            int eveningManagers = scanner.nextInt();
            System.out.print("Evening Employees: ");
            int eveningEmployees = scanner.nextInt();
            ShiftRules rules = new ShiftRules(morningManagers, morningEmployees, eveningManagers, eveningEmployees);
            scheduleService.setShiftRequirements(day, rules);
        }
        System.out.println("Shift requirements set successfully.");
    }

    public void generateWeeklySchedule() {
        String schedule = scheduleService.generateWeeklySchedule(constraintsService.getAllConstraints());
        System.out.println(schedule);
    }
}
