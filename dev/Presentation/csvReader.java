package Presentation;

import Domain.*;
import Service.ConstraintsService;
import Service.EmployeeService;
import Service.WorkArrangementService;
import Service.ScheduleService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class csvReader {
    public static void initializeData(String filePath, ConstraintsService constraintsService, EmployeeService employeeService, WorkArrangementService workArrangementService, ScheduleService scheduleService) {
        // Reading the CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String section = "";
            // List of employees
            ArrayList<Employee> employees = new ArrayList<>();
            // While the next line is not null, continue looping
            while ((line = br.readLine()) != null) {
                // When you get to the line that has "Employees:", set section to "Emp"
                if (line.equalsIgnoreCase("Employees")) {
                    section = "Emp";
                    continue;
                }

                String[] values = line.split(","); // Splits the line into an array of values based on commas
                switch (section) {
                    case "Emp":
                        // Parse the employee details
                        String id = values[0];
                        String fname = values[1];
                        String lname = values[2];
                        int salary = Integer.parseInt(values[3]);
                        LocalDate startDate = LocalDate.parse(values[4]);
                        String jobType = values[5];
                        String wage = values[6];
                        String daysOff = values[7];
                        String locationOfSuper = values[8];
                        String managerFullName = values[9];
                        String password = values[10];

                        // Create necessary objects
                        terms t = new terms(startDate, jobType, wage, daysOff);
                        SuperMarket superMarket = new SuperMarket(locationOfSuper, managerFullName);

                        // Create an employee and add to the list
                        Employee employee = new Employee(id, fname, lname, salary, t, superMarket);
                        employee.setPassword(password);

                        employees.add(employee);
                        break;
                }
            }

            // Register employees in EmployeeService
            for (Employee employee : employees) {
                employeeService.registerEmployee(employee.getFname(), employee.getLname(), employee.getId(), employee.getSalary(), employee.getTerms(), employee.getSuperMarketBranch());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
