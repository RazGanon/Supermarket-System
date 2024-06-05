package Presentation;

import Domain.Employee;
import Domain.SuperMarket;
import Service.*;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import static Presentation.csvReader.initializeData;

public class Main {
    private static EmployeeService employeeManagement = new EmployeeService();
    private static UserService userManagement = new UserService();
    private static Gson gson = new Gson();
    private static ConstraintsService constraintManagement = new ConstraintsService();
    private static ScheduleService scheduleService;
    private static WorkArrangementService workarrManagement;

    public static void main(String[] args) {
        try {
            // Example initialization of ScheduleService
            scheduleService = new ScheduleService("Store1", LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(17, 0), new SuperMarket("Location", "Manager"));

            csvReader.initializeData("resources/data.csv",constraintManagement,employeeManagement,workarrManagement,scheduleService);
        } catch (Exception e) {
            System.out.println("Error initializing data: " + e.getMessage());
        }

        // Create scanner object to read user input from the console
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter username: ");
            String username = scanner.nextLine();
            System.out.println("Enter password: ");
            String password = scanner.nextLine();

            // Verify the employee using the username (assuming username is the employee ID)
            Employee employeeToCheck = employeeManagement.getEmployeeById(username);
            if (employeeToCheck == null) {
                System.out.println("Invalid username Please try again.");
                continue;
            }
            if (!employeeToCheck.equals(password)){
                System.out.println("Invalid password Please try again.");
            continue;
        }

            boolean isManager = employeeManagement.isManager(employeeToCheck);
            if (isManager) {
                showMenuForManager(scanner);
            } else {
                showMenuForEmployee(scanner);
            }
        }
    }

    private static void watchCurrentSchedule() {
        String currentSchedule = scheduleService.getCurrentSchedule();
        System.out.println(currentSchedule);
    }

    private static void showMenuForManager(Scanner scanner) {
        while (true) {
            System.out.println("Hello Manager !! ");
            System.out.println("Menu:");
            System.out.println("1. Watch The Current Schedule");
            System.out.println("2. List Of Workers By SuperMarket Branch");
            System.out.println("3. Change Role Of Employee");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    watchCurrentSchedule();
                    break;
                case 2:
                    // Add functionality to list workers by branch
                    break;
                case 3:
                    // Add functionality to change role of employee
                    break;
                case 11:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showMenuForEmployee(Scanner scanner) {
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Make Your Constraint For The Next Schedule");
            System.out.println("2. Send Request For Change Constraints");
            System.out.println("3. Change Constraints Before The Time Ends");
            System.out.println("4. Watch Current Schedule");
            System.out.println("11. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Add functionality to make constraints for the next schedule
                    break;
                case 2:
                    // Add functionality to send request for change constraints
                    break;
                case 3:
                    // Add functionality to change constraints before the time ends
                    break;
                case 4:
                    watchCurrentSchedule();
                    break;
                case 11:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
