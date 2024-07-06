package Presentation;

import Data.DataSource;
import Domain.*;
import java.time.LocalDate;
import java.util.*;

public class Main {
    private EmployeeController employeeManagement;
    private static ScheduleController scheduleController;
    private ConstraintsController constraintsController;
    private Employee employeeToCheck = null;
    private boolean isRunning;

    public Main(EmployeeController employeeManagement, ScheduleController scheduleController, ConstraintsController constraintsController) {
        this.employeeManagement = employeeManagement;
        this.scheduleController = scheduleController;
        this.constraintsController = constraintsController;
        this.isRunning = true;

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmployeeController employeeManagement = new EmployeeController();
        ScheduleController scheduleController = new ScheduleController(employeeManagement, null);
        ConstraintsController constraintsController = new ConstraintsController(scheduleController);
        Main mainApp = new Main(employeeManagement, scheduleController, constraintsController);

        System.out.println("Do you want to clean data before running? (yes/no)");
        String cleanData = scanner.nextLine();

        if (cleanData.equalsIgnoreCase("yes")) {
            mainApp.setData();
        }

        //mainApp.initializeData();
        employeeManagement.addAllTAbleEmp();
        //SuperMarketController.addAllSuperMarketsToSystem();
        scheduleController.connectAllShiftsToSYS();

        while (true) {
            System.out.println("Enter username: ");
            String username = scanner.nextLine();
            System.out.println("Enter password: ");
            String password = scanner.nextLine();

            Employee employeeToCheck = mainApp.employeeManagement.getEmployeeById(username);
            if (employeeToCheck == null) {
                System.out.println("Invalid username. Please try again.");
                continue;
            }
            if (!employeeToCheck.getPassword().equals(password)) {
                System.out.println("Invalid password. Please try again.");
                continue;
            }

            boolean isManager = mainApp.employeeManagement.isManager(employeeToCheck);
            if (isManager) {
                mainApp.showMenuForManager(scanner);
            } else {
                mainApp.showMenuForEmployee(scanner);
            }
        }
    }

    private void initializeData() {
        this.scheduleController = new ScheduleController(employeeManagement, constraintsController);
        this.constraintsController = new ConstraintsController(scheduleController);
        employeeManagement.addAllTAbleEmp();
        SuperMarketController.addAllSuperMarketsToSystem();
        scheduleController.connectAllShiftsToSYS();
    }

    public void setData() {
        DataSource.cleanTables();
        DataSource.restoreSuperMarketStartingData();
        initializeData();
    }

    //IF ADMIN WANT TO REGISTER EMPLOYEE MANUALLY
    public void registerEmployee(Scanner scanner) {
        System.out.print("Enter employee first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter employee last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter employee ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter employee salary: ");
        int salary = Integer.parseInt(scanner.nextLine());
        String dayOff = "0"; // start with 0 days off

        String jobType = "";
        while (true) {
            System.out.print("Choose job type:\n1. Full-Time Job\n2. Part-Time Job\nEnter your choice: ");
            int jobTypeChoice = Integer.parseInt(scanner.nextLine());
            if (jobTypeChoice == 1) {
                jobType = "Full-Time Job";
                break;
            } else if (jobTypeChoice == 2) {
                jobType = "Part-Time Job";
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        // Show the list of supermarkets and let the user choose one
        System.out.println("Choose a supermarket from the following list:");
        List<SuperMarket> superMarkets = SuperMarketController.getAllSuperMarkets();
        for (int i = 0; i < superMarkets.size(); i++) {
            System.out.println((i + 1) + ". " + superMarkets.get(i));
        }
        SuperMarket chosenSuperMarket = null;
        while (true) {
            System.out.print("Enter your choice (number): ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 1 && choice <= superMarkets.size()) {
                chosenSuperMarket = superMarkets.get(choice - 1);
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        LocalDate date = LocalDate.now();
        String sdate = date.toString();
        terms newTerms = new terms(sdate, jobType, dayOff);

        // Assuming registerEmployeeManually takes these parameters
        employeeManagement.registerEmployeeManually(firstName, lastName, id, salary, newTerms, chosenSuperMarket);

        System.out.println("Employee registered successfully.");
    }

    private void deleteSuperMarket(Scanner scanner) {
        System.out.println("Choose a supermarket to delete from the following list:");
        List<SuperMarket> superMarkets = SuperMarket.getAllSuperMarkets();
        for (int i = 0; i < superMarkets.size(); i++) {
            System.out.println((i + 1) + ". " + superMarkets.get(i));
        }

        System.out.print("Enter your choice (number): ");
        int choice = Integer.parseInt(scanner.nextLine());
        if (choice >= 1 && choice <= superMarkets.size()) {
            SuperMarket chosenSuperMarket = superMarkets.get(choice - 1);
            boolean isRemoved = SuperMarket.removeSuperMarketByAddress(chosenSuperMarket.getAddress());
            if (isRemoved) {
                System.out.println("Supermarket at " + chosenSuperMarket.getAddress() + " has been deleted.");
            } else {
                System.out.println("Failed to delete the supermarket. Please try again.");
            }
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    private void addSuperMarket(Scanner scanner) {
        System.out.print("Congrats you had another supermarket: ");
        System.out.print("What is the address? ");
        String address = scanner.nextLine();
        System.out.print("What is the ID of the new manager? ");
        String managerId = scanner.nextLine();

        // Check if the manager is in the system
        Employee newManager = employeeManagement.getEmployeeById(managerId);
        if (newManager == null) {
            System.out.println("This ID is not in the system. Please try again.");
            return;
        }

        // Check employee role; if not manager, change role to manager
        Role role = newManager.getRole();
        if (role != Role.Manager) {
            newManager.setRole(Role.Manager);
            employeeManagement.changeEmpRole(newManager, Role.Manager);
        }

        // Add supermarket to the system
        SuperMarket newSuperMarket = new SuperMarket(address, newManager.getFname() + " " + newManager.getLname());
        employeeManagement.addSupermarket(address, newManager.getFname() + " " + newManager.getLname());
        employeeManagement.updateEmployeeSuperMarketBranch(managerId, newSuperMarket);
        System.out.println("Supermarket at " + address + " with manager " + newManager.getFname() + " " + newManager.getLname() + " has been added.");
    }

    private void changeEmpRole(Scanner scanner) {
        System.out.print("Enter employee ID: ");
        String idChoice = scanner.nextLine();
        Employee e = employeeManagement.getEmployeeById(idChoice);
        if (e == null) {
            System.out.println("Employee not found. Please try again.");
            return;
        }

        List<Role> roleList = new ArrayList<>(EnumSet.allOf(Role.class));
        System.out.println("Available roles:");
        for (int i = 0; i < roleList.size(); i++) {
            System.out.println((i + 1) + ": " + roleList.get(i));
        }

        System.out.print("Please choose a role by entering the corresponding number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice >= 1 && choice <= roleList.size()) {
            Role selectedRole = roleList.get(choice - 1);
            employeeManagement.changeEmpRole(e, selectedRole);
            System.out.println("Role updated successfully.");
        } else {
            System.out.println("Invalid choice. Please run the program again and choose a valid number.");
        }
    }

    private void generateSchedule() {
        Scanner scanner = new Scanner(System.in);
        constraintsController.setShiftRequirements(scanner);
        constraintsController.generateWeeklySchedule();
    }

    private void checkEmployeeRole() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter The employee ID That You Want To Check Is Role : ");
        String idChoice = scanner.nextLine();
        employeeManagement.getRoleEmployeeById(idChoice);
    }

    private void checkConstraints(Scanner scanner) {
        System.out.print("Enter employee ID: ");
        String idChoice = scanner.nextLine();
        Employee e = employeeManagement.getEmployeeById(idChoice);
        if (e == null) {
            System.out.println("Employee not found. Please try again.");
            return;
        }
        System.out.print("Enter Week Num (Start From 0): ");
        int weekChoice = Integer.parseInt(scanner.nextLine());

        constraintsController.printConstraintsForEmployee(idChoice, weekChoice);
    }

    private void PrintEmpPastConstraints(Scanner scanner, String id) {
        Employee e = employeeManagement.getEmployeeById(id);
        if (e == null) {
            System.out.println("Employee not found. Please try again.");
            return;
        }
        System.out.print("Enter Week Num (Start From 0): ");
        int weekChoice = Integer.parseInt(scanner.nextLine());

        constraintsController.printConstraintsForEmployee(id, weekChoice);
    }

    private void checkRelevantConstraints(Scanner scanner) {
        System.out.print("Enter employee ID: ");
        String idChoice = scanner.nextLine();
        Employee e = employeeManagement.getEmployeeById(idChoice);
        if (e == null) {
            System.out.println("Employee not found. Please try again.");
            return;
        }
        int weekChoice = scheduleController.getWeekFlag();
        constraintsController.printConstraintsForEmployee(idChoice, weekChoice);
    }

    private void showAllEmployeesInSuperMarket(Scanner scanner) {
        System.out.print("Enter the location of the supermarket: ");
        String location = scanner.nextLine();
        List<Employee> employees = employeeManagement.getAllEmployeesInSuperMarket(location);
        if (employees.isEmpty()) {
            System.out.println("No employees found in this supermarket.");
        } else {
            System.out.println("Employees in supermarket at " + location + ":");
            for (Employee employee : employees) {
                System.out.println(employee.getFname() + " " + employee.getLname() + " (ID: " + employee.getId() + ")");
            }
        }
    }

    private void ShowWeekFlag() {
        System.out.println("The Week Number is: " + scheduleController.getWeekFlag());
    }


    private void showMenuForManager(Scanner scanner) {
        while (isRunning) {
            System.out.println("Hello Manager !!");
            System.out.println("Menu:");
            System.out.println("1. Watch The Current Schedule");
            System.out.println("2. Change Role Of Employee");
            System.out.println("3. Check Employee Constraints By Week");
            System.out.println("4. Check Employee Relevant Constraints");
            System.out.println("5. Generate Schedule By Your Rules");
            System.out.println("6. Get Employee Role By Id");
            System.out.println("7. Allow submission");
            System.out.println("8. Do not allow submission");
            System.out.println("9. Register New Employee");
            System.out.println("10. Add SuperMarket To The System");
            System.out.println("11. Show me all The Employees in my supermarket");
            System.out.println("12. Show me all The Employees Constraints");
            System.out.println("13. Show me all The SuperMarkets");
            System.out.println("14. What is the number of the week?");
            System.out.println("15. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    watchCurrentSchedule();
                    break;
                case 2:
                    changeEmpRole(scanner);
                    break;
                case 3:
                    checkConstraints(scanner);
                    break;
                case 4:
                    checkRelevantConstraints(scanner);
                    break;
                case 5:
                    generateSchedule();
                    break;
                case 6:
                    checkEmployeeRole();
                    break;
                case 7:
                    constraintsController.startSubmission();
                    break;
                case 8:
                    constraintsController.stopSubmission();
                    break;
                case 9:
                    registerEmployee(scanner);
                    break;
                case 10:
                    addSuperMarket(scanner);
                    break;
                case 11:
                    showAllEmployeesInSuperMarket(scanner);
                    break;
                case 12:
                    constraintsController.printAllRelevantConstraints();
                    break;
                case 13:
                    SuperMarketController.printAllSuperMarkets();
                    break;
                case 14:
                    ShowWeekFlag();
                    System.out.println("When you Start new Session Of submission the week is grown by 1");
                    break;
                case 15:
                    System.out.println("logging out...");
                case 16:
                    isRunning =false;
                    System.out.println("Exiting from system...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void showMenuForEmployee(Scanner scanner) {
        while (isRunning) {
            System.out.println("Menu:");
            System.out.println("1. Make Your Constraint For The Next Schedule");
            System.out.println("2. Watch Current Schedule");
            System.out.println("3. View My Updated Constraints");
            System.out.println("4. View Past Constraints By Week Num");
            System.out.println("5. What Is My Role ? ");
            System.out.println("6. View Past Schedules");
            System.out.println("7. What is the number of the week?");
            System.out.println("11. log out ");
            System.out.println("11. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    constraintsController.addConstraints(employeeToCheck.getId(), scanner);
                    break;
                case 2:
                    watchCurrentSchedule();
                    break;
                case 3:
                    constraintsController.printConstraintsForEmployee(employeeToCheck.getId(), scheduleController.getWeekFlag());
                    break;
                case 4:
                    PrintEmpPastConstraints(scanner, employeeToCheck.getId());
                    break;
                case 5:
                    Role r = employeeManagement.getRoleEmployeeById(employeeToCheck.getId());
                    break;
                case 7:
                    ShowWeekFlag();
                    break;
                case 11:
                    System.out.println("logging out ...");
                    return;
                case 12:
                    System.out.println("Exiting from system...");
                    isRunning=false;
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Getter for EmployeeController to use in tests
    public EmployeeController getEmployeeManagement() {
        return employeeManagement;
    }
    private static void watchCurrentSchedule() {
        String currentSchedule = scheduleController.getCurrentSchedule();
        System.out.println(currentSchedule);
    }
}
