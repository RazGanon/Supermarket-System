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
                if (line.equalsIgnoreCase("SuperMarkets")) {
                    section = "Sm";
                    continue;
                }
                if (line.equalsIgnoreCase("")){
                section = "end";}

                String[] values = line.split(","); // Splits the line into an array of values based on commas
                switch (section) {
                    case "Emp":
                        // Parse the employee details
                        String id = values[0];
                        String fname = values[1];
                        String lname = values[2];
                        int salary = Integer.parseInt(values[3]);
                        LocalDate startDate = LocalDate.parse(values[4]);
                        String role = values[5];
                        String wage = values[6];
                        String daysOff = values[7];
                        String locationOfSuper = values[8];
                        String managerFullName = values[9];
                        String password = values[10];

                        // Create necessary objects
                        terms t = new terms(startDate, role , daysOff);
                        SuperMarket superMarket = new SuperMarket(locationOfSuper, managerFullName);
                        Role r = Role.valueOf(role);

                        // Create an employee and add to the list
                        Employee employee;
                        employee = employeeService.registerEmployeeAuto(fname, lname, id, salary, t, superMarket,password,r);
                        //employee.setPassword(password);

                        employees.add(employee);
                        break;
                    case "Sm":
                        // Parse the supermarket details
                        String address = values[0];
                        String manager = values[1];

                        // Create a supermarket and add to the list if not already present
                        SuperMarket existingSuperMarket = SuperMarket.findSuperMarketByAddress(address);
                        if (existingSuperMarket == null) {
                            SuperMarket newSuperMarket = new SuperMarket(address, manager);
                            SuperMarket.addSuperMarket(newSuperMarket);
                        }
                        break;

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
