package Presentation;
import Domain.*;
import Service.*;
import com.google.gson.Gson;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Main {
    private static final EmployeeService employeeManagement = new EmployeeService();
    private static final UserService userManagement = new UserService();
    private static final Gson gson = new Gson();
    private static final ConstraintsService constraintManagement = new ConstraintsService();
    static ScheduleService scheduleService;
    //private static final WorkArrangementService workarrManagement = new WorkArrangementService();
    private static ConstraintsController constraintsController;
    private static String user_id;
    private static Employee employeeToCheck = null;

    public static void main(String[] args) {
        try {
            scheduleService = new ScheduleService("Store1", LocalDate.now(), LocalTime.of(8, 0), LocalTime.of(12, 0), LocalTime.of(13, 0), LocalTime.of(17, 0), new SuperMarket("Location", "Manager"), employeeManagement);
            constraintsController = new ConstraintsController(constraintManagement, scheduleService);
            csvReader.initializeData("resources/data.csv", constraintManagement, employeeManagement,  scheduleService);
        } catch (Exception e) {
            System.out.println("Error initializing data: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter username: ");
            String username = scanner.nextLine();
            user_id = username;
            System.out.println("Enter password: ");
            String password = scanner.nextLine();

            employeeToCheck = employeeManagement.getEmployeeById(username);
            if (employeeToCheck == null) {
                System.out.println("Invalid username. Please try again.");
                continue;
            }
            if (!employeeToCheck.getPassword().equals(password)) {
                System.out.println("Invalid password. Please try again.");
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

    //IF ADMIN WANT TO REGISTER EMPLOYEE MANUALLY
    private static void registerEmployee(Scanner scanner) {
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
        List<SuperMarket> superMarkets = SuperMarket.getAllSuperMarkets();
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
        terms newTerms = new terms(date, jobType,dayOff);

        // Assuming registerEmployeeManually takes these parameters
        employeeManagement.registerEmployeeManually(firstName, lastName, id, salary,newTerms,chosenSuperMarket);

        System.out.println("Employee registered successfully.");
    }
    private static void deleteSuperMarket(Scanner scanner) {
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

    private static void addSuperMarket(Scanner scanner) {
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
        }

        // Add supermarket to the system
        SuperMarket newSuperMarket = new SuperMarket(address, newManager.getFname() + " " + newManager.getLname());
        employeeManagement.addSupermarket(address, managerId);
        employeeManagement.updateEmployeeSuperMarketBranch(managerId, newSuperMarket);
        System.out.println("Supermarket at " + address + " with manager " + newManager.getFname() + " " + newManager.getLname() + " has been added.");
    }
    private static void changeEmpRole(Scanner scanner) {
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
    private static void generateSchedule() {
        Scanner scanner = new Scanner(System.in);
        constraintsController.setShiftRequirements(scanner);
        constraintsController.generateWeeklySchedule();
    }
    private static void checkEmployeeRole() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter The employee ID That You Want To Check Is Role : ");
        String idChoice = scanner.nextLine();
        employeeManagement.getRoleEmployeeById(idChoice);}


        private static void checkConstraints(Scanner scanner) {
        System.out.print("Enter employee ID: ");
        String idChoice = scanner.nextLine();
        Employee e = employeeManagement.getEmployeeById(idChoice);
        if (e == null) {
            System.out.println("Employee not found. Please try again.");
            return;
        }
        constraintsController.printConstraintsForEmployee(idChoice);
    }
    private static void showAllEmployeesInSuperMarket(Scanner scanner) {
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
    private static void addAndRetrieveSchedules(Employee employee) {
        Shift shift1 = new Shift("Morning", "08:00", "16:00");
        Shift shift2 = new Shift("Evening", "16:00", "00:00");

        Schedule schedule1 = new Schedule(LocalDate.now(), Arrays.asList(shift1, shift2));
        Schedule schedule2 = new Schedule(LocalDate.now().minusDays(1), Arrays.asList(shift1));

        employee.addSchedule(schedule1);
        employee.addSchedule(schedule2);

        List<Schedule> pastSchedules = employee.getPastSchedules();
        System.out.println("Past schedules for employee " + employee.getFname() + " " + employee.getLname() + ":");
        for (Schedule schedule : pastSchedules) {
            System.out.println("Date: " + schedule.getDate());
            for (Shift shift : schedule.getShifts()) {
                System.out.println("  Shift: " + shift.getShiftType() + " from " + shift.getStartTime() + " to " + shift.getEndTime());
            }
        }
    }
    private static void showPastSchedulesForEmployee(Employee employee) {
        List<Schedule> pastSchedules = employee.getPastSchedules();
        if (pastSchedules.isEmpty()) {
            System.out.println("No past schedules found for employee " + employee.getFname() + " " + employee.getLname());
        } else {
            System.out.println("Past schedules for employee " + employee.getFname() + " " + employee.getLname() + ":");
            for (Schedule schedule : pastSchedules) {
                System.out.println("Date: " + schedule.getDate());
                for (Shift shift : schedule.getShifts()) {
                    System.out.println("  Shift: " + shift.getShiftType() + " from " + shift.getStartTime() + " to " + shift.getEndTime());
                }
            }
        }
    }
    private static void showMenuForManager(Scanner scanner) {
        while (true) {
            System.out.println("Hello Manager !!");
            System.out.println("Menu:");
            System.out.println("1. Watch The Current Schedule");
            System.out.println("2. Change Role Of Employee");
            System.out.println("3. Check Employee Constraints");
            System.out.println("4. Generate Schedule By Your Rules");
            System.out.println("5. Get Employee Role By Id");
            System.out.println("6. Allow submission");
            System.out.println("7. Do not allow submission");
            System.out.println("8. Register New Employee");
            System.out.println("9. Add SuperMarket To The System");
            System.out.println("10. Show me all The Employees in my supermarket");
            System.out.println("11. Show me all The Employees Constraints");
            System.out.println("12. Exit");
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
                    generateSchedule();
                    break;
                case 5:
                    checkEmployeeRole();
                    break;
                case 6:
                    constraintsController.StartSubmission();
                    break;
                case 7:
                    constraintsController.stopSubmission();
                    break;
                case 8:
                    registerEmployee(scanner);
                    break;
                case 9:
                    addSuperMarket(scanner);
                    break;
                case 10:
                    showAllEmployeesInSuperMarket(scanner);
                    break;
                case 11:
                    constraintsController.printAllConstraints();
                    break;
                case 12:
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
            System.out.println("2. Watch Current Schedule");
            System.out.println("3. Print My Updated Constraints");
            System.out.println("4. What Is My Role ? ");
            System.out.println("5. View Past Schedules");
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
                    constraintsController.printConstraintsForEmployee(employeeToCheck.getId());
                    break;
                case 4:
                Role r = employeeManagement.getRoleEmployeeById(employeeToCheck.getId());
                break;
                case 5:
                    showPastSchedulesForEmployee(employeeToCheck);
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
