package Domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EmployeeController {
    // Create a map of employees
    static int numberOfSuperMarket = 0 ;
    private Map<String, Employee> employeeMap = new HashMap<>();
    private Map<String,SuperMarket > Supermarketsmap = new HashMap<>();

    // Method to register an employee
    public void registerEmployee(String fName, String lName, String Id, int Salary, terms Terms, SuperMarket superMarketBranch) {
        if (Id == null || fName == null || lName == null || Terms == null) {
            System.out.println("Invalid input. All fields are required.");
            return;
        }

        if (!checkID(Id)) { // if ID is already in the system
            System.out.println("Employee with ID: " + Id + " already exists.");
            return;
        }

        Employee newEmployee = new Employee(fName, lName, Id, Salary, Terms, superMarketBranch);
        try {
            employeeMap.put(newEmployee.getId(), newEmployee);
            System.out.println("Employee registered successfully: " + newEmployee);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to check if an ID already exists
    public boolean checkID(String id) {
        if (employeeMap.containsKey(id)) {
            System.out.println("Employee with ID: " + id + " already exists.");
            return false; // if employee is already in the system, return false
        } else {
            return true;
        }
    }

    public Employee getEmployeeById(String id) {
        if (!employeeMap.containsKey(id)) {
            System.out.println("Employee with ID: " + id + " is not exists.");
            return null; // if employee is not in the system
        }
        Employee Emp = employeeMap.get(id);
        return Emp;
    }
    public terms setTerms(Date startdate,String jobtype, String wage,String daysoff) {
        return new terms(startdate, jobtype, wage, daysoff);
    }
    public SuperMarket addsupermarket(String address,String ManagerName) {
        SuperMarket newS = new SuperMarket(address, ManagerName);
        Supermarketsmap.put(String.valueOf(numberOfSuperMarket), newS);
        numberOfSuperMarket++;
        return newS;
    }
    public boolean isManager(Employee e){
        Role empR = e.getRole();
        if (empR.get_Role_Name() == "Manager"){
            return true;
        }
        else return false;
    }
    public void printListEmpl(){
        System.out.println("\nThe Map Contains Following Elements: \n\n" + employeeMap);


            }





    }


