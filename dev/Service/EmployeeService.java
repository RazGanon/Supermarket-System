package Service;

import java.time.LocalDate;
import java.util.*;

import Domain.Employee;
import Domain.Role;
import Domain.SuperMarket;
import Domain.terms;

public class EmployeeService {
    static int numberOfSuperMarket = 0;
    private Map<String, Employee> employeeMap = new HashMap<>();
    private Map<String, SuperMarket> supermarketsMap = new HashMap<>();

    // Method to register an employee
    public Employee registerEmployee(String fName, String lName, String id, int salary, terms terms, SuperMarket superMarketBranch) {
        if (id == null || fName == null || lName == null || terms == null) {
            System.out.println("Invalid input. All fields are required.");
            return null;
        }

        if (!checkID(id)) { // if ID is already in the system
            System.out.println("Employee with ID: " + id + " already exists.");
            return null;
        }

        Employee newEmployee = new Employee(fName, lName, id, salary, terms, superMarketBranch);
        try {
            employeeMap.put(newEmployee.getId(), newEmployee);
            System.out.println("Ask the employee what password they want: ");
            Scanner scanner = new Scanner(System.in);
            String newEmpPassword = scanner.nextLine();
            newEmployee.setPassword(newEmpPassword); // employee chose password and we added it to his info
            System.out.println("Employee registered successfully: " + newEmployee);
            return newEmployee;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to check if an ID already exists
    public boolean checkID(String id) {
        return !employeeMap.containsKey(id);
    }

    public Employee getEmployeeById(String id) {
        if (!employeeMap.containsKey(id)) {
            System.out.println("Employee with ID: " + id + " does not exist.");
            return null; // if employee is not in the system
        }
        return employeeMap.get(id);
    }

    public terms setTerms(LocalDate startdate, String jobtype, String wage, String daysoff) {
        return new terms(startdate, jobtype, wage, daysoff);
    }

    public SuperMarket addSupermarket(String address, String managerName) {
        SuperMarket newS = new SuperMarket(address, managerName);
        supermarketsMap.put(String.valueOf(numberOfSuperMarket), newS);
        numberOfSuperMarket++;
        return newS;
    }

    public boolean isManager(Employee e) {
        Role empR = e.getRole();
        return empR == Role.Manager;
    }

    public void printListEmpl() {
        System.out.println("\nThe Map Contains Following Elements: \n\n" + employeeMap);
    }
}
