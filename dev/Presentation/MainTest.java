package Presentation;

import static org.junit.jupiter.api.Assertions.*;

import Data.DataSource;
import Domain.*;
import Presentation.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.Scanner;

public class MainTest {
    private static EmployeeController employeeController;
    private static ScheduleController scheduleController;
    private static ConstraintsController constraintsController;
    private Main main;
    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @BeforeAll
    public static void setUpClass() {
        employeeController = new EmployeeController();
        constraintsController = new ConstraintsController(null);
        scheduleController = new ScheduleController(employeeController, constraintsController);
    }

    @BeforeEach
    public void setUp() {
        main = new Main(employeeController, scheduleController, constraintsController);
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
        System.setIn(originalSystemIn); // Reset System.in to its original state
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    public void testMainWithFullSequence() {
        // Simulate input for the entire sequence of interactions
        String input = "yes\n" +   // Clean data before running
                "1234\n" +  // Enter username
                "1234\n" ; // Enter password
        provideInput(input);

        // Act
        Main.main(new String[]{});

        // Assert
        String output = testOut.toString();
        System.out.println(output); // Print the output for debugging
        assertTrue(output.contains("ConstraintsTable table has been cleaned."));
        assertTrue(output.contains("schedule table has been cleaned."));
        assertTrue(output.contains("schedule_shift_employee table has been cleaned."));
        assertTrue(output.contains("WeekFlag table has been cleaned."));
        assertTrue(output.contains("Employee table has been cleaned."));
        assertTrue(output.contains("SuperMarkets table has been cleaned."));
        assertTrue(output.contains("Manager employee has been added to the Employee table"));
        assertTrue(output.contains("Starting data has been restored."));
        assertTrue(output.contains("Enter username:"));
        assertTrue(output.contains("Enter password:"));
        // Add more assertions as needed to verify the state after these interactions
    }

    @Test
    public void cleanDataTest() {
        // Simulate clean data and then fill it
        String input =
                "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "1122\n";

        provideInput(input);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        DataSource.cleanTables(); // clean all the tables except supermarkets table
        DataSource.cleanTable("SuperMarkets"); // clean supermarkets table

        assertTrue(out.toString().contains("SuperMarkets table has been cleaned."));

        DataSource.restoreSuperMarketStartingData();
        employeeController.addAllTAbleEmp();
        SuperMarketController.addAllSuperMarketsToSystem();
        assertTrue(out.toString().contains("Starting data has been restored."));

        // Check if employee is in the system
        Employee employee = EmployeeController.getEmployeeById("1234");
        assertNotNull(employee);
        assertEquals("mor", employee.getFname());
    }

    @Test
    public void testUserExperience() {
        // Simulate clean data and then fill it
        String input =
                "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "yes\n" +
                        "1122\n";

        provideInput(input);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        DataSource.cleanTables(); // clean all the tables except supermarkets table
        DataSource.cleanTable("SuperMarkets"); // clean supermarkets table
        DataSource.restoreSuperMarketStartingData();
        employeeController.addAllTAbleEmp();
        SuperMarketController.addAllSuperMarketsToSystem();
        assertTrue(out.toString().contains("Starting data has been restored."));


        // Simulate adding an employee
        String input1 = "shon\n" +   // emp first name
                "platok\n" +   // emp last name
                "1122\n" +   // emp id
                "10000\n" +   // emp salary
                "1\n" +   // chose full time job
                "3\n" +   // Chose the third supermarket in the list
                "1122\n";  // Enter password

        provideInput(input1);

        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));

        // Act
        main.registerEmployee(new Scanner(System.in));

        // Assert
        Employee employee = EmployeeController.getEmployeeById("1122");
        assertNotNull(employee);
        assertEquals("shon", employee.getFname());
        assertTrue(out.toString().contains("Employee registered successfully."));
    }
}
