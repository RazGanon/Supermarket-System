package Presentation;

import Domain.Employee;
import Domain.Role;
import Domain.SuperMarket;
import Service.*;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static Presentation.csvReader.initializeData;

public class Main {
    private static EmployeeService employeeManagement = new EmployeeService();
    private static UserService userManagement = new UserService();
    private static Gson gson = new Gson();
    private static ConstraintsService constraintManagement = new ConstraintsService();
    private static ScheduleService scheduleService;
    private static WorkArrangementService workarrManagement;
    private static String user_id;

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
            user_id=username;
            System.out.println("Enter password: ");
            String password = scanner.nextLine();

            // Verify the employee using the username (assuming username is the employee ID)
            Employee employeeToCheck = employeeManagement.getEmployeeById(username);
            if (employeeToCheck == null) {
                System.out.println("Invalid username Please try again.");
                continue;
            }
            if (!employeeToCheck.getPassword().equals(password)){
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
    public static void PrintListOfEmplByBranch(){


    }
    public static void ChangeEmpRole(){
        Role selectedRole = null;
        //requst emp id
        //use user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("add employee id");
        String idchoice = String.valueOf(scanner.nextInt());
        //find this emp by his id :
        Employee e =employeeManagement.getEmployeeById(idchoice);


        //ask user to choose the new role for emp
            // Get all roles in the Role enum
        EnumSet<Role> allRoles = EnumSet.allOf(Role.class);
            // Convert EnumSet to List
        List<Role> roleList = allRoles.stream().collect(Collectors.toList());
            // Print the list of roles
        roleList.forEach(System.out::println);
        // Print the list of roles
        System.out.println("Available roles:");
        for (int i = 0; i < roleList.size(); i++) {
            System.out.println((i + 1) + ": " + roleList.get(i));
        }

        // Prompt the user to choose a role
        System.out.print("Please choose a role by entering the corresponding number: ");
        int choice1 = scanner.nextInt();

        // Validate the user's choice
        if (choice1 >= 1 && choice1 <= roleList.size()) {
            selectedRole = roleList.get(choice1 - 1);
            System.out.println("You have selected: " + selectedRole);
        } else {
            System.out.println("Invalid choice. Please run the program again and choose a valid number.");
        }

        employeeManagement.changeEmpRole(e,selectedRole);
    }

    private static void ManagerAddEmpToShift() {
        Scanner scanner = new Scanner(System.in);

        // Request employee ID
        System.out.println("Enter employee ID:");
        String idChoice = scanner.nextLine();

        // Find employee by ID
        Employee e = employeeManagement.getEmployeeById(idChoice);
        if (e == null) {
            System.out.println("Employee not found. Please try again.");
            return;
        }

        // Prompt for day with validation
        int day = 0;
        while (day < 1 || day > 6) {
            System.out.println("In which day?");
            System.out.println("1. Sunday");
            System.out.println("2. Monday");
            System.out.println("3. Tuesday");
            System.out.println("4. Wednesday");
            System.out.println("5. Thursday");
            System.out.println("6. Friday");
            day = scanner.nextInt();
            if (day < 1 || day > 6) {
                System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }

        // Prompt for shift with validation
        int shift = 0;
        while (shift < 1 || shift > 2) {
            System.out.println("In which shift?");
            System.out.println("1. Morning");
            System.out.println("2. Evening");
            shift = scanner.nextInt();
            if (shift < 1 || shift > 2) {
                System.out.println("Invalid choice. Please enter 1 for Morning or 2 for Evening.");
            }
        }

        // Assume 'bi' is a flag indicating the employee can work on this shift
        int bi = 1;

        // Add constraint to employee
        constraintManagement.addConstraint(idChoice, shift, day, bi);
        System.out.println("Constraint added successfully.");
    }


    private static void showMenuForManager(Scanner scanner) {
        while (true) {
            System.out.println("Hello Manager !! ");
            System.out.println("Menu:");
            System.out.println("1. Watch The Current Schedule");
            System.out.println("2. List Of Workers By SuperMarket Branch");
            System.out.println("3. Change Role Of Employee");
            System.out.println("3. Add employee to shift ");

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
                    ChangeEmpRole();
                    break;
                case 4:
                    ManagerAddEmpToShift();
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
                    constraintManagement.getConstraintFromUser();
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
