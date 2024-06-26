package Domain;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class EmployeeController {
    static int numberOfSuperMarket = 0;
    //this map contains id,employee object
    private Map<String, Employee> employeeMap = new HashMap<>();
    //this map contain adrress of supermarket, supermarket object
    private Map<String, SuperMarket> supermarketsMap = new HashMap<>();

    // Method to register an employee

    // here i build two register employee function -- one of them is when the admin want to add employee manually and than he need to
    // ask the new employee what password he want , and the other is when i build employee automatically
    public Employee registerEmployeeManually(String fName, String lName, String id, int salary, terms terms, SuperMarket superMarketBranch) {
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
    public List<Employee> getAllEmployeesInSuperMarket(String superMarketLocation) {
        return employeeMap.values().stream()
                .filter(employee -> employee.getSuperMarketBranch().getAddress().equals(superMarketLocation))
                .collect(Collectors.toList());
    }
    public Employee registerEmployeeAuto(String fName, String lName, String id, int salary, terms terms, SuperMarket superMarketBranch,String password,Role r) {
        if (id == null || fName == null || lName == null || terms == null || password == null||r == null ) {
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
            newEmployee.setPassword(password); // employee chose password and we added it to his info
            newEmployee.setRole(r);
            //System.out.println("Employee registered successfully: " + newEmployee);
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
    public Role getRoleEmployeeById(String id) {
        Role R ;
        if (!employeeMap.containsKey(id)) {
            System.out.println("Employee with ID: " + id + " does not exist.");
            return null; // if employee is not in the system
        }
        R = employeeMap.get(id).getRole();
        System.out.println(R);
        return R;
    }
    public terms setTerms(LocalDate startdate, String jobtype, String wage, String daysoff) {
        return new terms(startdate, jobtype, daysoff);
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
    public void changeEmpRole(Employee e,Role r){
        e.setRole(r);
    }

    public void printListEmpl() {
        System.out.println("\nThe Map Contains Following Elements: \n\n" + employeeMap);
    }
    public void printempConstraints(Employee e) {
        System.out.println("\n:the constraints of\n"+e.getFname()+" " + e.getConstraint());
    }
    // Method to get all employees
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeeMap.values());
    }
    // Method to update an employee's supermarket branch
    public void updateEmployeeSuperMarketBranch(String employeeId, SuperMarket newSuperMarket) {
        Employee employee = employeeMap.get(employeeId);
        if (employee != null) {
            employee.setSuperMarket(newSuperMarket);
        }

    }

}
